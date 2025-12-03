package com.divinelink.core.fixtures

private val loremIpsumText = """
  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec et ipsum porttitor,
  consectetur arcu at, dignissim diam. Cras molestie felis quis nunc suscipit,
  quis feugiat nunc dignissim. Donec quam metus, pretium ut nunc vitae, maximus
  suscipit nunc. Vestibulum iaculis rhoncus tellus, non tristique eros aliquam in.
  Proin nec laoreet dui, nec volutpat magna. Aenean iaculis, erat sit amet mattis dignissim,
  dui tortor vehicula eros, eget pharetra massa tellus a eros. Praesent non dolor a nulla
  vehicula posuere. Donec id nisi non arcu dapibus faucibus ut ac dolor. 
  Aliquam in imperdiet mauris, non vehicula lectus. Aliquam in egestas nulla.
  Aliquam a sem quis ante dictum finibus. Praesent maximus mauris mauris,
  maximus lacinia tellus fringilla at. Phasellus sed tristique nibh, id
  egestas ante. Sed tempor magna in tellus maximus, id euismod nisi ultrices. 
  Donec aliquam eleifend odio vitae interdum. Mauris molestie ante non sem commodo ornare.

  Maecenas consectetur tortor ut est lobortis aliquet. Morbi in massa commodo turpis consectetur 
  fringilla eu sed metus. Sed quis risus velit. Cras dapibus velit eget ligula tincidunt, at
  eleifend urna suscipit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis vel 
  nunc enim. Proin lacinia, arcu eget convallis semper, mi est tempor massa, sit amet 
  consequat dui justo nec mauris. Nulla luctus urna eget gravida porttitor. In arcu purus,
  blandit ut consequat sit amet, suscipit nec velit. Fusce a enim ut urna viverra ullamcorper
  non sollicitudin quam. Nunc elementum rutrum ultrices. Phasellus ante eros, laoreet vitae
  venenatis a, pulvinar ac magna. Ut auctor dolor nec libero congue viverra. Suspendisse
  potenti. Ut ante sem, egestas at tempor at, fermentum et nunc.

  Nulla quis odio imperdiet, efficitur tortor ultrices, cursus neque. Orci varius natoque 
  penatibus et magnis dis parturient montes, nascetur ridiculus mus. Morbi
  mollis ligula at interdum blandit. Pellentesque nunc turpis, semper ac viverra ac,
  scelerisque eget quam. Maecenas volutpat leo eleifend facilisis fringilla. 
  Nam nec ante et purus vehicula placerat sit amet sed nibh. Maecenas pulvinar 
  venenatis nibh, sed mattis felis dictum vehicula. Curabitur fringilla commodo augue,
  vitae pellentesque odio lacinia et. In eu velit enim. Mauris quam ipsum, varius nec magna in,
  porttitor rutrum elit. Nunc imperdiet ullamcorper quam, ut consectetur est commodo sed.
""".trimIndent()

fun loremIpsum(take: Int): String {
  if (take <= 0 || loremIpsumText.isBlank()) return ""

  return loremIpsumText
    .split(Regex("\\s+"))
    .take(take)
    .joinToString(" ")
}
