package com.divinelink.core.ui.extension

fun String.format(vararg args: Any): String {
  var result = this
  args.forEach { arg ->
    result = result.replaceFirst("%s", arg.toString())
  }
  return result
}
