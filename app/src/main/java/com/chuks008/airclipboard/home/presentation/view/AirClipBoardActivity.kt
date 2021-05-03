package com.chuks008.airclipboard.home.presentation.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.chuks008.airclipboard.R
import com.chuks008.airclipboard.home.MainApplication
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipBoardViewModel
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardState
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardConnectSuccess
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardTextSent
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardConnectClosed
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardDiscoverState
import com.chuks008.airclipboard.home.presentation.viewmodel.ClipboardConnectError

class AirClipBoardActivity: AppCompatActivity(), DIAware {

    override lateinit var di: DI

    private lateinit var connectBtn: MaterialButton
    private lateinit var disconnectBtn: MaterialButton
    private lateinit var sendBtn: MaterialButton
    private lateinit var rescanBtn: MaterialButton
    private lateinit var statusText: TextView
    private lateinit var viewModel: ClipBoardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        di = (this@AirClipBoardActivity.applicationContext as MainApplication).di
        val internalViewModel: ClipBoardViewModel by di.instance()
        viewModel = internalViewModel

        connectBtn = findViewById(R.id.connect_button)
        disconnectBtn = findViewById(R.id.disconnect_button)
        sendBtn = findViewById(R.id.send_button)
        statusText = findViewById(R.id.status_text)
        rescanBtn = findViewById(R.id.scan_again_button)

        connectBtn.setOnClickListener {
            viewModel.connectToClipBoard()
            statusText.text = "Connecting..."
        }

        disconnectBtn.setOnClickListener {
            viewModel.disconnectFromClipboard()
        }

        sendBtn.setOnClickListener {
            viewModel.sendTextToClipboard("Happy!!")
        }

        rescanBtn.setOnClickListener {
            viewModel.startClipboardDiscovery()
            rescanBtn.isEnabled = false
        }

        rescanBtn.isEnabled = false
        viewModel.startClipboardDiscovery()

        viewModel.eventListener.observe(this@AirClipBoardActivity) { clipboardState ->
            handleUI(clipboardState)
        }
    }

    private fun handleUI(clipboardState: ClipboardState?) {
        when (clipboardState) {
            is ClipboardConnectSuccess -> {
                statusText.text = "Connection Success!!"
                statusText.setTextColor(Color.GREEN)
                connectBtn.visibility = View.GONE
                disconnectBtn.visibility = View.VISIBLE
                sendBtn.visibility = View.VISIBLE
            }
            is ClipboardConnectError -> {
                statusText.text = "No clipboard to connect to"
                statusText.setTextColor(Color.RED)
                hideClipboardOptions()
            }
            is ClipboardTextSent -> {
                statusText.text = "Text sent: "
                statusText.setTextColor(Color.GREEN)
                connectBtn.visibility = View.GONE
                disconnectBtn.visibility = View.VISIBLE
            }

            is ClipboardConnectClosed -> {
                statusText.text = "Connection closed..."
                statusText.setTextColor(Color.RED)
                connectBtn.visibility = View.VISIBLE
                disconnectBtn.visibility = View.GONE
                sendBtn.visibility = View.GONE
            }

            is ClipboardDiscoverState -> {
                if(clipboardState.isFound) {
                    showClipboardOptions()
                } else {
                    Toast.makeText(this, "Unable to find server, try again", Toast.LENGTH_SHORT).show()
                    hideClipboardOptions()
                }
            }
        }
    }

    private fun hideClipboardOptions() {
        connectBtn.visibility = View.GONE
        sendBtn.visibility = View.GONE
        disconnectBtn.visibility = View.GONE
        rescanBtn.visibility = View.VISIBLE
        rescanBtn.isEnabled = true
        statusText.text = "No clipboards found"
    }

    private fun showClipboardOptions() {
        connectBtn.visibility = View.VISIBLE
        sendBtn.visibility = View.GONE
        disconnectBtn.visibility = View.GONE
        rescanBtn.visibility = View.GONE
        statusText.text = "Found 1 clipboard"
    }
}
