package de.jonasrottmann.realmbrowser.files

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.RestrictTo
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import de.jonasrottmann.realmbrowser.R
import de.jonasrottmann.realmbrowser.extensions.toast
import de.jonasrottmann.realmbrowser.helper.DataHolder
import de.jonasrottmann.realmbrowser.models.ModelsActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException
import kotterknife.bindView
import timber.log.Timber

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class FilesActivity : AppCompatActivity(), FilesAdapter.OnFileSelectedListener {

    private lateinit var adapter: FilesAdapter
    private lateinit var viewModel: FilesViewModel
    private val swipeRefreshLayout: SwipeRefreshLayout by bindView(R.id.realm_browser_swiperefresh)
    private val recyclerView: RecyclerView by bindView(R.id.realm_browser_recycler)
    private val toolbar: Toolbar by bindView(R.id.realm_browser_toolbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.realm_browser_ac_recycler)
        setSupportActionBar(toolbar)

        // Adapter init
        adapter = FilesAdapter(this, this)
        recyclerView.adapter = adapter

        // ViewModel
        viewModel = ViewModelProviders.of(this).get(FilesViewModel::class.java)
        viewModel.files.observe(this, Observer { filesPojos ->
            Timber.d("onChange...")
            adapter.swapList(filesPojos ?: ArrayList())
            swipeRefreshLayout.isRefreshing = false
        })

        // Disable SwipeRefreshLayout - not used in this Activity
        swipeRefreshLayout.setColorSchemeResources(R.color.realm_browser_dark_purple)
        swipeRefreshLayout.isRefreshing = true
        swipeRefreshLayout.setOnRefreshListener { viewModel.refreshFiles() }
    }

    override fun onFileSelected(file: FilesPojo) {
        try {
            val config = RealmConfiguration.Builder().name(file.name).build()
            DataHolder.getInstance().save(DataHolder.DATA_HOLDER_KEY_CONFIG, config)
            val realm = Realm.getInstance(config)
            realm.close()
            startActivity(ModelsActivity.getIntent(this))
        } catch (e: RealmMigrationNeededException) {
            toast("${getString(R.string.realm_browser_open_error)} ${getString(R.string.realm_browser_error_migration)}")
        } catch (e: RealmFileException) {
            toast("${getString(R.string.realm_browser_open_error)} ${e.message}")
        } catch (e: Exception) {
            toast("${getString(R.string.realm_browser_open_error)} ${getString(R.string.realm_browser_error_openinstances)}")
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, FilesActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return intent
        }
    }
}
