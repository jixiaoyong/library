package cf.android666.diywidget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import cf.android666.applibrary.view.Toast
import kotlinx.android.synthetic.main.activity_toast.*


/**
 * author: jixiaoyong
 * email: jixiaoyong1995@gmail.com
 * website: https://jixiaoyong.github.io
 * date: 2019-07-17
 * description: todo
 */
class ToastActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast)

        button.setOnClickListener {
            Toast.show("Hello ${count++}", Toast.ToastType.WARNING, Gravity.BOTTOM)
        }
    }

}

