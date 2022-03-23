package com.kemsky.dipaypokedex.ui.activity.detailpokemon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.kemsky.dipaypokedex.R
import com.kemsky.dipaypokedex.ViewModelFactory
import com.kemsky.dipaypokedex.constant.AppConstant.colorByType
import com.kemsky.dipaypokedex.constant.AppConstant.getImageUrl
import com.kemsky.dipaypokedex.databinding.ActivityDetailBinding
import com.kemsky.dipaypokedex.helper.Resource
import com.kemsky.dipaypokedex.helper.decimetersToMeters
import com.kemsky.dipaypokedex.helper.hectogramsToKilograms
import com.kemsky.dipaypokedex.helper.setImageSrcFromUrlWithLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.job
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.ceil

// Entry point dependency injection by dagger hilt
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    // injecting dependency (viewmodelfactory)
    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: DetailViewModel

    private var binding: ActivityDetailBinding? = null

    private val pokeId by lazy {
        intent.extras?.getInt("poke_id", 0)
    }

    private val pokeName by lazy {
        intent.extras?.getString("poke_name")?.replaceFirstChar(Char::titlecase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflating UI by viewbinding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        configureToolbar()

        // instantiating viewmodel with viewmodelprovider with custom factory
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        configureUi()
    }

    private fun configureToolbar() {
        binding?.toolbar?.title = pokeName
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        invalidateOptionsMenu()
    }

    private fun configureUi() {
        lifecycleScope.launchWhenStarted {
            pokeId?.let {
                viewModel.fetchDetailPokemon(it).combine(viewModel.fetchSpeciesPokemon(it)) { detail, species ->
                    with(binding!!) {
                        when (detail) {
                            is Resource.Loading -> {
                                content.visibility = View.GONE
                                loadingProgress.visibility = View.VISIBLE
                                binding?.textFlavour?.text = getString(R.string.loading)
                            }
                            is Resource.Error -> {
                                content.visibility = View.GONE
                                loadingProgress.visibility = View.GONE
                                binding?.textFlavour?.text = getString(R.string.error)
                            }
                            else -> {
                                content.visibility = View.VISIBLE
                                loadingProgress.visibility = View.GONE

                                // Detail data like stats, type, et cetera
                                detail.data?.let { data ->
                                    avatarPokemon.setImageSrcFromUrlWithLoader(
                                        getImageUrl(data.id.toString()),
                                        lavLoader
                                    )
                                    btnFav.setOnClickListener { configureFavoriteButton() }
                                    txtWeight.text = String.format(
                                        "%s %s",
                                        data.weight.hectogramsToKilograms().toString(),
                                        "Kg"
                                    )
                                    txtHeight.text = String.format(
                                        "%s %s",
                                        data.height.decimetersToMeters().toString(),
                                        "m"
                                    )

                                    if (!data.types.isNullOrEmpty()) {
                                        data.types.forEach {
                                            val chip = Chip(this@DetailActivity)
                                            chip.apply {
                                                setTextColor(
                                                    ContextCompat.getColor(
                                                        this@DetailActivity,
                                                        R.color.white
                                                    )
                                                )
                                                chipEndPadding = 12f
                                                text =
                                                    it.type.name.replaceFirstChar(Char::titlecase)
                                                chipBackgroundColor = colorByType(this@DetailActivity, it.type.name)
                                            }
                                            binding?.linearChip?.addView(chip)
                                        }
                                        content.setBackgroundColor(colorByType(this@DetailActivity, data.types[0].type.name).defaultColor)
                                    }


                                    data.stats.forEach { stats ->
                                        when (stats.stat.name) {
                                            "hp" -> {
                                                progressHp.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtHp.text = stats.baseStat.toString()
                                            }
                                            "attack" -> {
                                                progressAtk.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtAtk.text = stats.baseStat.toString()
                                            }
                                            "defense" -> {
                                                progressDef.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtDef.text = stats.baseStat.toString()
                                            }
                                            "special-attack" -> {
                                                progressSpAtk.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtSpAtk.text = stats.baseStat.toString()
                                            }
                                            "special-defense" -> {
                                                progressSpDef.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtSpDef.text = stats.baseStat.toString()
                                            }
                                            else -> {
                                                Timber.i(
                                                    ceil(
                                                        stats.baseStat.div(255).times(100)
                                                            .toDouble()
                                                    ).toInt().toString()
                                                )
                                                progressSpd.setProgressCompat(
                                                    ceil(stats.baseStat.toDouble() / 255 * 100).toInt(),
                                                    true
                                                )
                                                txtSpd.text = stats.baseStat.toString()
                                            }
                                        }

                                    }

                                }

                                // Species Flavor Text
                                val sb = StringBuffer()
                                species.data?.flavorTextEntries?.distinctBy { text ->
                                    text.flavorText
                                }?.forEachIndexed { index, flavorTextEntry ->
                                    if (index < 3) {
                                        sb.append("${flavorTextEntry.flavorText.replace("\n", " ")} ")
                                    }
                                }
                                binding?.textFlavour?.text = sb.toString()
                            }
                        }
                    }
                }.filterNotNull().collect()
            }
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem = menu?.findItem(R.id.pokemon_number)
        if (menuItem?.title.toString().equals("NUMBER", ignoreCase = true)) {
            menuItem?.title = "#$pokeId"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Another possibility is to call 'finish()'
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureFavoriteButton() {
        lifecycleScope.launchWhenStarted {
            viewModel.getSingleFav(pokeId)?.collect { model ->
                if (model?.id != pokeId) {
                    model?.let {
                        viewModel.addToFavorite(it)
                    }
                    Toast.makeText(
                        this@DetailActivity,
                        "$pokeName telah ditambahkan ke Favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.btnFav?.setImageResource(R.drawable.ic_favorite_24)
                    // To stop reactivity in flow
                    this.coroutineContext.job.cancelAndJoin()
                } else {
                    model?.let { viewModel.deleteFromFavorite(it) }
                    Toast.makeText(
                        this@DetailActivity,
                        "$pokeName telah dihapus dari Favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.btnFav?.setImageResource(R.drawable.ic_unfavorite_24)
                    // To stop reactivity in flow
                    this.coroutineContext.job.cancelAndJoin()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Avoiding memory leak in databinding/viewbinding
        binding = null
    }
}