package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding.inflate
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                likesAmount.text = amountChecker(post.likes)
                likes.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24dp else R.drawable.ic_baseline_favorite_border_24dp)
                sharesAmount.text = amountChecker(post.shares)
            }
        }

        binding.likes.setOnClickListener {
            viewModel.like()
        }

        binding.shares.setOnClickListener {
            viewModel.share()
        }

    }
}

fun amountChecker(value: Int): String {     // проверка на количество лайков/репостов/просмотров
    val newAmount = value.toString()
    val delimiter = '.'
    return if (value in 1000..9999) {       // 1.1К отображается по достижении 1100
        if (value in 1000..1099) {
            newAmount.dropLast(3) + "K"
        } else {
            newAmount.dropLast(3) + delimiter + newAmount.dropLast(2).drop(1) + "K"
        }
    } else if (value in 10000..999999) {     // после 10К сотни перестают отображаться
        newAmount.dropLast(3) + "K"
    } else if (value in 1000000..Int.MAX_VALUE) {   // после 1M сотни тысяч выводим в формате 1.3M
        when (value) {
            in 1000000..1099999 -> {
                newAmount.dropLast(6) + "M"
            }
            in 1100000..9999999 -> {
                newAmount.dropLast(6) + delimiter + newAmount.dropLast(5).drop(1) + "M"
            }
            in 10000000..999999999 -> {   // от 10M до 1000М число в формате ххM или хххM (сотни тысяч не выводим)
                newAmount.dropLast(6) + "M"
            }
            in 1000000000..Int.MAX_VALUE -> {  // После 1 миллиарда и до maxInt выводим '$$$' - откуда там столько лайков/репостов/просмотров?
                "\$\$\$"
            }
            else -> {
                newAmount
            }
        }
    } else {
        newAmount
    }
}