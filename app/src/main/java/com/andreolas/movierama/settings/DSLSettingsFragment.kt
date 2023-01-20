package com.andreolas.movierama.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreolas.movierama.R
import dagger.hilt.android.AndroidEntryPoint
import gr.divinelink.core.util.extensions.addSystemWindowInsetToMargin

@AndroidEntryPoint
open class DSLSettingsFragment() : Fragment() {
    private lateinit var callback: Callback

    @StringRes
    var titleId: Int = -1

    @MenuRes
    var menuId: Int = -1

    @LayoutRes
    var layoutId: Int = R.layout.dsl_settings_fragment

    var layoutManagerProducer: (Context) -> RecyclerView.LayoutManager = { context -> LinearLayoutManager(context) }

    protected var recyclerView: RecyclerView? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: Toolbar? = view.findViewById(R.id.toolbar)

        if (titleId != -1) {
            toolbar?.setTitle(titleId)
        }
        toolbar?.addSystemWindowInsetToMargin(top = true)

        toolbar?.setNavigationOnClickListener {
            onToolbarNavigationClicked()
        }

        if (menuId != -1) {
            toolbar?.inflateMenu(menuId)
            toolbar?.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        }

        val settingsAdapter = DSLSettingsAdapter()

        recyclerView = view.findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = layoutManagerProducer(requireContext())
            adapter = settingsAdapter
        }

        bindAdapter(settingsAdapter)
    }

    open fun onToolbarNavigationClicked() {
        callback.onBackPressed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback
    }

    open fun bindAdapter(adapter: DSLSettingsAdapter) {
        // Intentionally Blank
    }

    interface Callback {
        fun onBackPressed()
    }

    constructor(
        @StringRes titleId: Int,
        @MenuRes menuId: Int,
        @LayoutRes layoutId: Int,
        //        layoutManagerProducer: (Context) -> RecyclerView.LayoutManager,
    ) : this() {
        this.titleId = titleId
        this.menuId = menuId
        this.layoutId = layoutId
        //        this.layoutManagerProducer = layoutManagerProducer
    }

    constructor(
        @StringRes titleId: Int,
    ) : this() {
        this.titleId = titleId
    }
}
