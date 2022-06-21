package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding.*
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 9999,
            likedByMe = false,
            shares = 997
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likes.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24dp else R.drawable.ic_baseline_favorite_border_24dp)

            likes.setOnClickListener {
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                likesAmount.text = amountChecker(post.likes)
                likes.setImageResource(if (post.likedByMe) R.drawable.ic_baseline_favorite_24dp else R.drawable.ic_baseline_favorite_border_24dp)
            }

            shares.setOnClickListener {
                post.shares++
                sharesAmount.text = amountChecker(post.shares)
            }
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