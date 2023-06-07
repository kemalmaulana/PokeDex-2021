package com.kemsky.pokedex.presentation.ui.activity.detailpokemon

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.kemsky.pokedex.R
import com.kemsky.pokedex.core.constant.AppConstant.colorByType
import com.kemsky.pokedex.core.constant.AppConstant.getImageUrl
import com.kemsky.pokedex.data.room.model.FavPokemonModel
import com.kemsky.pokedex.databinding.ActivityDetailBinding
import com.kemsky.pokedex.core.helper.Resource
import com.kemsky.pokedex.core.helper.decimetersToMeters
import com.kemsky.pokedex.core.helper.hectogramsToKilograms
import com.kemsky.pokedex.core.helper.setImageSrcFromUrlWithLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.math.ceil

// Entry point dependency injection by dagger hilt
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()

    private var binding: ActivityDetailBinding? = null

    private val pokeId by lazy {
        intent.extras?.getInt("poke_id", 0)
    }

    private val pokeName by lazy {
        intent.extras?.getString("poke_name")?.replaceFirstChar(Char::titlecase)
    }

    private val formatter: DecimalFormat = DecimalFormat("#000")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflating UI by viewbinding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        configureToolbar()
        configureUi()
    }

    private fun configureToolbar() {
        binding?.toolbar?.title = pokeName
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        invalidateOptionsMenu()
    }

    private fun configureUi() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokeId?.let {
                    viewModel.fetchDetailPokemon(it)
                        .combine(viewModel.fetchSpeciesPokemon(it)) { detail, species ->
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
                                                        text =
                                                            it.type.name.replaceFirstChar(Char::titlecase)
                                                        chipBackgroundColor = colorByType(
                                                            this@DetailActivity,
                                                            it.type.name
                                                        )
                                                    }
                                                    binding?.linearChip?.addView(chip)
                                                }
                                                content.setBackgroundColor(
                                                    colorByType(
                                                        this@DetailActivity,
                                                        data.types[0].type.name
                                                    ).defaultColor
                                                )
                                            }


                                            data.stats?.forEach { stats ->
                                                when (stats.stat.name) {
                                                    "hp" -> {
                                                        progressHp.setProgressCompat(
                                                            stats.baseStat,
                                                            true
                                                        )
                                                        txtHp.text = stats.baseStat.toString()
                                                    }
                                                    "attack" -> {
                                                        progressAtk.setProgressCompat(
                                                            stats.baseStat,
                                                            true
                                                        )
                                                        txtAtk.text = stats.baseStat.toString()
                                                    }
                                                    "defense" -> {
                                                        progressDef.setProgressCompat(
                                                            stats.baseStat,
                                                            true
                                                        )
                                                        txtDef.text = stats.baseStat.toString()
                                                    }
                                                    "special-attack" -> {
                                                        progressSpAtk.setProgressCompat(
                                                            stats.baseStat,
                                                            true
                                                        )
                                                        txtSpAtk.text = stats.baseStat.toString()
                                                    }
                                                    "special-defense" -> {
                                                        progressSpDef.setProgressCompat(
                                                            stats.baseStat,
                                                            true
                                                        )
                                                        txtSpDef.text = stats.baseStat.toString()
                                                    }
                                                    else -> {
                                                        Timber.e("SPD: ${ceil(stats.baseStat.toDouble() / 255 * 100).toInt()}")
                                                        progressSpd.setProgressCompat(
                                                            stats.baseStat,
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
                                        }?.filter { text ->
                                            text.language.name == "en"
                                        }?.forEachIndexed { index, flavorTextEntry ->
                                            if (index < 3) {
                                                sb.append(
                                                    "${
                                                        flavorTextEntry.flavorText.replace(
                                                            "\n",
                                                            " "
                                                        )
                                                    } "
                                                )
                                            }
                                        }
                                        binding?.textFlavour?.text = sb.toString()
                                    }
                                }
                            }
                        }.filterNotNull().collect()

                    viewModel.getSingleFav(it)?.collect { model ->
                        if (model?.id != null) {
                            binding?.btnFav?.setImageResource(R.drawable.ic_favorite_24)
                            binding?.btnFav?.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@DetailActivity,
                                    R.color.pink
                                )
                            )
                        } else {
                            binding?.btnFav?.setImageResource(R.drawable.ic_unfavorite_24)
                        }
                    }
                }
            }
        }


    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.pokemon_number)
        if (menuItem?.title.toString().equals("NUMBER", ignoreCase = true)) {
            menuItem?.title = String.format("#%s", formatter.format(pokeId))
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Another possibility is to call 'finish()'
//                onBackPressed()
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun configureFavoriteButton() {
        lifecycleScope.launch {
            viewModel.getSingleFav(pokeId)?.collect {
                Timber.e("from Database : ${it?.id}")
                Timber.e("to input : $pokeId")
                if (it?.id != pokeId) {
                    val newFav = FavPokemonModel(pokeId!!, pokeName!!)
                    viewModel.addToFavorite(newFav)
                    Toast.makeText(
                        this@DetailActivity,
                        "$pokeName telah ditambahkan ke Favorite",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.btnFav?.setImageResource(R.drawable.ic_favorite_24)
                    binding?.btnFav?.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.pink
                        )
                    )
                    // To stop reactivity in flow
                    this.coroutineContext.job.cancelAndJoin()
                } else {
                    it?.let { viewModel.deleteFromFavorite(it) }
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