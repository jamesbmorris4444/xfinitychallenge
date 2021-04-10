package com.sample.commonlibrary.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.sample.commonlibrary.activity.databinding.ActivityMainBinding
import com.sample.commonlibrary.characters.CharactersFragment
import com.sample.commonlibrary.characters.CharactersListViewModel
import com.sample.commonlibrary.individual.IndividualFragment
import com.sample.commonlibrary.individual.IndividualViewModel
import com.sample.commonlibrary.logger.LogUtils
import com.sample.commonlibrary.repository.Repository
import com.sample.commonlibrary.services.LongRunningService
import com.sample.commonlibrary.services.ServiceCallbacks
import com.sample.commonlibrary.ui.UIViewModel
import com.sample.commonlibrary.utils.Constants
import com.sample.commonlibrary.utils.DaggerViewModelDependencyInjector
import com.sample.commonlibrary.utils.Utils
import com.sample.commonlibrary.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


open class MainActivity : AppCompatActivity(), Callbacks, ServiceCallbacks {

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var activityMainBinding: ActivityMainBinding
    private var thumbsStatusMenuItem: MenuItem? = null
    private lateinit var charactersFragment: CharactersFragment
    private lateinit var individualFragment: IndividualFragment
    val imageHeight: ObservableField<Int> = ObservableField(0)
    val imageWidth: ObservableField<Int> = ObservableField(0)

    enum class UITheme {
        LIGHT, DARK, NOT_ASSIGNED,
    }

    lateinit var currentTheme: UITheme

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository(this)
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(this))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.uiViewModel = uiViewModel
        activityMainBinding.mainActivity = this
        setSupportActionBar(toolbar)
        setTabletScreens()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { charactersFragment.charactersListViewModel.backPressed() }
        lottieBackgroundView = main_background_lottie
        loadInitialFragment()
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }
        serviceProgressBar = service_progresss_bar
    }

    private fun setTabletScreens() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        imageHeight.set(size.y / 2)
        imageWidth.set(size.x / 2)
    }

    // Start Service

    private val TAG = MainActivity::class.java.simpleName
    lateinit var longRunningService: LongRunningService
    private lateinit var serviceProgressBar: ProgressBar

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            LogUtils.D(
                TAG,
                LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
                String.format("MainActivity: onremoteServiceConnected()")
            )
            val binder = service as LongRunningService.LocalBinder
            longRunningService = binder.getService()
            longRunningService.setServiceCallbacks(this@MainActivity)
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            LogUtils.D(
                TAG,
                LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
                String.format("MainActivity: onServiceDisconnected()")
            )
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
        uiViewModel.lottieAnimation(
            lottieBackgroundView,
            uiViewModel.backgroundLottieJsonFileName,
            LottieDrawable.INFINITE
        )

        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: bindService in onResume()")
        )
        val intent = Intent(this, LongRunningService::class.java)
        startService(intent)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        loadCharactersFragment()
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: onStop unbindService()")
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LongRunningService::class.java))
        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: onDestroy stopService()")
        )
    }

    fun startPretendLongRunningTask() {
        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: startPretendLongRunningTask()")
        )
        longRunningService.startPretendLongRunningTask()
    }

    fun pausePretendLongRunningTask() {
        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: pausePretendLongRunningTask()")
        )
        longRunningService.pausePretendLongRunningTask()
    }

    fun resumePretendLongRunningTask() {
        LogUtils.D(
            TAG,
            LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM),
            String.format("MainActivity: resumePretendLongRunningTask()")
        )
        longRunningService.resumePretendLongRunningTask()
    }

    override fun setServiceProgress(progress: Int) {
        serviceProgressBar.progress = progress
    }

    override fun setProgressMaxValue(maxValue: Int) {
        serviceProgressBar.max = maxValue

    }

    // End Service

    fun getMainProgressBar(): ProgressBar {
        return main_progress_bar
    }

    private fun loadInitialFragment() {
        if (supportFragmentManager.findFragmentByTag(Constants.ROOT_FRAGMENT_TAG) == null) {
            loadCharactersFragment()
        }
    }

    private fun loadCharactersFragment() {
        charactersFragment = CharactersFragment.newInstance()
        val container: Int
        if (Utils.isTablet(this)) {
            if (Utils.isPortrait(this)) {
                fetchRootView().main_activity_container.visibility = View.GONE
                fetchRootView().upper_lower_root.visibility = View.VISIBLE
                fetchRootView().left_right_root.visibility = View.GONE
                container = R.id.main_activity_top_container
            } else {
                fetchRootView().main_activity_container.visibility = View.GONE
                fetchRootView().upper_lower_root.visibility = View.GONE
                fetchRootView().left_right_root.visibility = View.VISIBLE
                container = R.id.main_activity_left_container
            }
        } else {
            fetchRootView().main_activity_container.visibility = View.VISIBLE
            fetchRootView().upper_lower_root.visibility = View.GONE
            fetchRootView().left_right_root.visibility = View.GONE
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            container = R.id.main_activity_container
        }
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(container, charactersFragment, Constants.ROOT_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    fun loadIndividualFragment(name: String, url: String, description: String) {
        individualFragment = IndividualFragment.newInstance(name, url, description)
        val container: Int
        if (Utils.isTablet(this)) {
            if (Utils.isPortrait(this)) {
                container = R.id.main_activity_bottom_container
            } else {
                container = R.id.main_activity_right_container
            }
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(container, individualFragment, Constants.ROOT_FRAGMENT_TAG)
                .commitAllowingStateLoss()
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            container = R.id.main_activity_container
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(container, individualFragment, Constants.ROOT_FRAGMENT_TAG)
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val backArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(backArrow);
            toolbar.setTitleTextColor(Color.parseColor(uiViewModel.toolbarTextColor))
        }
    }

    private fun colorizeToolbarOverflowButton(toolbar: Toolbar, color: Int): Boolean {
        val overflowIcon = toolbar.overflowIcon ?: return false
        toolbar.overflowIcon = getTintedDrawable(overflowIcon, color)
        return true
    }

    private fun getTintedDrawable(inputDrawable: Drawable, color: Int): Drawable {
        val wrapDrawable = DrawableCompat.wrap(inputDrawable)
        DrawableCompat.setTint(wrapDrawable, color)
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN)
        return wrapDrawable
    }

    private fun setToolbarThumbStatus(resInt: Int) {
        thumbsStatusMenuItem?.let { thumbsStatusMenuItem ->
            runOnUiThread {
                thumbsStatusMenuItem.icon = ContextCompat.getDrawable(this, resInt)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_toggle_theme -> {
            if (currentTheme == UITheme.LIGHT) {
                currentTheme = UITheme.DARK
            } else {
                currentTheme = UITheme.LIGHT
            }
            uiViewModel.currentTheme = currentTheme
            uiViewModel.lottieAnimation(
                lottieBackgroundView,
                uiViewModel.backgroundLottieJsonFileName,
                LottieDrawable.INFINITE
            )
            setupToolbar()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchCharactersListViewModel() : CharactersListViewModel {
        return CharactersListViewModel(
        this
    ) }

    override fun fetchIndividualViewModel() : IndividualViewModel {
        return IndividualViewModel(this)
    }

}