# fastlane/helpers/codeberg_helper.rb

require 'net/http'
require 'json'
require 'uri'
require 'date'

module CodebergHelper
  def self.get_previous_tag
    begin
      tag = `git describe --tags --abbrev=0 HEAD^`.strip
      return tag
    rescue => e
      UI.message("No previous tag found (this might be the first release).")
      return nil
    end
  end

  def self.fetch_merged_prs(repo_owner:, repo_name:, api_token:, from_tag: nil, to_tag: nil)
    cutoff_date = nil

    if from_tag.nil?
      from_tag = get_previous_tag
    end

    if from_tag
      begin
        tag_date_str = `git log -1 --format=%ai #{from_tag}`.strip
        cutoff_date = DateTime.parse(tag_date_str)
        UI.message("Using cutoff date from tag #{from_tag}: #{cutoff_date}")
      rescue => e
        UI.message("Warning: Could not get date for tag #{from_tag}: #{e.message}")
      end
    end

    url = URI("https://codeberg.org/api/v1/repos/#{repo_owner}/#{repo_name}/pulls?state=closed&sort=updated&order=desc&limit=100")

    http = Net::HTTP.new(url.host, url.port)
    http.use_ssl = true

    request = Net::HTTP::Get.new(url)
    request['Authorization'] = "token #{api_token}"
    request['Content-Type'] = 'application/json'

    response = http.request(request)

    if response.code != '200'
      UI.message("Warning: Failed to fetch PRs: #{response.code}")
      return []
    end

    prs = JSON.parse(response.body)

    merged_prs = prs.select do |pr|
      next false unless pr['merged_at'] && !pr['merged_at'].nil?

      if cutoff_date
        merge_date = DateTime.parse(pr['merged_at'])
        merge_date > cutoff_date
      else
        true
      end
    end

    UI.message("Found #{merged_prs.length} merged PRs since #{from_tag || 'beginning'}")

    return merged_prs
  end

  def self.generate_pr_release_notes(merged_prs:)
    return "No merged pull requests found." if merged_prs.empty?

    features = []
    fixes = []
    improvements = []
    docs = []
    other = []

    merged_prs.each do |pr|
      title = pr['title']
      labels = pr['labels'].map { |l| l['name'].downcase } rescue []

      if labels.any? { |l| l.include?('feature') || l.include?('enhancement') } ||
         title =~ /feat/i || title =~ /add/i
        features << pr
      elsif labels.any? { |l| l.include?('bug') || l.include?('fix') } ||
            title =~ /fix/i || title =~ /bug/i
        fixes << pr
      elsif labels.any? { |l| l.include?('documentation') || l.include?('docs') } ||
            title =~ /doc/i
        docs << pr
      elsif labels.any? { |l| l.include?('improvement') || l.include?('refactor') } ||
            title =~ /improve/i || title =~ /refactor/i
        improvements << pr
      else
        other << pr
      end
    end

    notes = []

    unless features.empty?
      notes << "## 🚀 New Features"
      features.each do |pr|
        notes << "- ##{pr['number']} #{pr['title']} (@#{pr['user']['login']})"
      end
    end

    unless fixes.empty?
      notes << "\n## 🐛 Bug Fixes"
      fixes.each do |pr|
        notes << "- ##{pr['number']} #{pr['title']} (@#{pr['user']['login']})"
      end
    end

    unless improvements.empty?
      notes << "\n## ✨ Improvements"
      improvements.each do |pr|
        notes << "- ##{pr['number']} #{pr['title']} (@#{pr['user']['login']})"
      end
    end

    unless docs.empty?
      notes << "\n## 📚 Documentation"
      docs.each do |pr|
        notes << "- ##{pr['number']} #{pr['title']} (@#{pr['user']['login']})"
      end
    end

    unless other.empty?
      notes << "\n## 📝 Other Changes"
      other.each do |pr|
        notes << "- ##{pr['number']} #{pr['title']} (@#{pr['user']['login']})"
      end
    end

    notes.join("\n")
  end
end