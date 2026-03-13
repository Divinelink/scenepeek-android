import UIKit
import app

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        InitializeKt.setup(isDebug: isDebug())

        if let url = launchOptions?[.url] as? URL {
            ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
        }

        window = UIWindow(frame: UIScreen.main.bounds)
        if let window = window {
            window.rootViewController = MainKt.MainViewController()
            window.makeKeyAndVisible()
        }
        return true
    }

    func application(
        _ application: UIApplication,
        continue userActivity: NSUserActivity,
        restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void
    ) -> Bool {
        if userActivity.activityType == NSUserActivityTypeBrowsingWeb,
           let url = userActivity.webpageURL {
            ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
            return true
        }
        return false
    }

    func application(
        _ application: UIApplication,
        open uri: URL,
        options: [UIApplication.OpenURLOptionsKey: Any] = [:]
    ) -> Bool {
        ExternalUriHandler.shared.onNewUri(uri: uri.absoluteString)
        return true
    }
}

private func isDebug() -> Bool {
    #if DEBUG
    return true
    #else
    return false
    #endif
}
