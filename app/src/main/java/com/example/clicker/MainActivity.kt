package com.example.clicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create references for all textViews
        val clickText: TextView = findViewById(R.id.points)
        val strCostText: TextView = findViewById(R.id.upgradeStrCost)
        val autoSpdCost: TextView = findViewById(R.id.autoSpdCost)
        val autoStrCost: TextView = findViewById(R.id.autoStrCost)

        // create references for buttons and images
        val coins: ImageView = findViewById(R.id.clicker)
        val clickStrButton: Button = findViewById(R.id.upgrade_str)
        val autoSpdButton: Button = findViewById(R.id.upgrade_auto_spd)
        val autoStrButton: Button = findViewById(R.id.upgradeAutoStr)

        val clicker = Clicker()

        // Calls click() to give the player points whenever the coins are clicked
        coins.setOnClickListener {
            clicker.click(clickText)
        }
        /* The following three OnClickListeners are attached to the upgrade
        buttons on screen. They call a corresponding upgrade function within
        the Clicker class. If the player does not have sufficient points,
        a Toast will be created to alert the player to the deficit.
         */
        clickStrButton.setOnClickListener {
            if (!clicker.upgradeClick()) {
                Toast.makeText(this, "You don't have enough points!", Toast.LENGTH_SHORT).show()
            } else {
                clickText.text = "${clicker.points}"
                strCostText.text = getString(R.string.updated_cost, clicker.strCost)
            }
        }
        autoSpdButton.setOnClickListener {
            if (!clicker.upgradeAutoSpd()) {
                Toast.makeText(this, "You don't have enough points!", Toast.LENGTH_SHORT).show()
            } else {
                clickText.text = "${clicker.points}"
                autoSpdCost.text = getString(R.string.updated_cost, clicker.autoSpdCost)
            }
        }
        autoStrButton.setOnClickListener {
            if (!clicker.upgradeAutoStr()) {
                Toast.makeText(this, "You don't have enough points!", Toast.LENGTH_SHORT).show()
            } else {
                clickText.text = "${clicker.points}"
                autoStrCost.text = getString(R.string.updated_cost, clicker.autoStrCost)
            }
        }


        val mainHandler = Handler(Looper.getMainLooper())
        /*
        Set up a loop that will call the autoClick function continuously.
        The delay between function calls is decreased as the player upgrades
        the autoClicker.
         */
        mainHandler.post(object : Runnable {
            override fun run() {
                clicker.autoClick(clickText)
                mainHandler.postDelayed(this, (2000 / clicker.autoClickSpeed).toLong())

            }
        })
    }
}
/* Helper class to manage variables such as click strength and auto clicker speed.
Also applies upgrades to variables when called and is responsible for updating points
when coins are clicked.
 */
class Clicker {
    var points = 0
    var clickStrength = 1
    var autoClickSpeed = 1
    var autoClickStrength = 1
    var strCost = 25
    var autoStrCost = 25
    var autoSpdCost = 25

    // called when coins are clicked. Gives the player points according to "clickStrength"
    fun click(clickText: TextView) {
        points += clickStrength
        clickText.text = "$points"
    }
    /* functions as click() but is used by the Handler class to
    continuously call a click at a given interval.
     */

    fun autoClick(clickText: TextView) {
        if (autoClickSpeed > 1) {
            points += autoClickStrength
            clickText.text = "$points"
        }
    }
    /* The following upgrade functions are called when the corresponding
     upgrade button is clicked. The functions update the players points,
     the cost of the next upgrade, and apply the upgrade itself. They only
     apply upgrades if the player has sufficient points.
     */
    fun upgradeClick(): Boolean {
        return if (points >= strCost) {
            points -= strCost
            clickStrength *= 2
            strCost *= 3
            true
        } else {false}
    }

    fun upgradeAutoSpd(): Boolean {
        return if (points >= autoSpdCost) {
            points -= autoSpdCost
            autoClickSpeed *= 2
            autoSpdCost *= 3
            true
        } else {false}
    }

    fun upgradeAutoStr(): Boolean {
        return if (points >= autoStrCost) {
            points -= autoStrCost
            autoClickStrength *= 2
            autoStrCost *= 3
            true
        } else {false}
    }
}