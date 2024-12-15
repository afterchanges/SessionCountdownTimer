package com.example.sessiontimer

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.util.Consumer
import com.intellij.util.concurrency.AppExecutorUtil
import java.awt.event.MouseEvent
import java.awt.Component
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.swing.SwingUtilities

class SessionCountdownWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.Multiframe, Disposable {

    private val widgetId = "com.example.sessiontimer.widget"
    private var sessionSeconds = 1500
    private var totalSeconds = sessionSeconds
    private var isPaused = false
    private var timerTask: ScheduledFuture<*>? = null
    private var statusBar: StatusBar? = null

    init {
        startTimerTask()
        Disposer.register(project, this)
    }

    private fun startTimerTask() {
        val executor = AppExecutorUtil.getAppScheduledExecutorService()
        timerTask?.cancel(true)
        timerTask = executor.scheduleWithFixedDelay({
            if (!isPaused) {
                if (totalSeconds > 0) {
                    totalSeconds -= 1
                    ApplicationManager.getApplication().invokeLater {
                        statusBar?.updateWidget(ID())
                    }
                } else if (totalSeconds == 0) {
                    // Timer has run out, show a message
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showMessageDialog(
                            project,
                            "Время сеанса истекло! Сделайте перерыв или начните новый сеанс.",
                            "Таймер завершён",
                            Messages.getInformationIcon()
                        )
                    }
                    // Prevent repeated triggers
                    isPaused = true
                }
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun getDisplayText(): String {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val stateChar = if (isPaused) "⏸" else "⏳"
        return "$stateChar %02d:%02d".format(minutes, seconds)
    }

    override fun ID(): String = widgetId

    override fun getPresentation(): StatusBarWidget.WidgetPresentation {
        return object : StatusBarWidget.TextPresentation {
            override fun getText(): String = getDisplayText()
            override fun getTooltipText(): String = "Left-click for actions"
            override fun getAlignment(): Float = 0.5f

            override fun getClickConsumer(): Consumer<MouseEvent>? {
                return Consumer { e ->
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        showPopupMenu(e)
                    }
                }
            }
        }
    }

    private fun showPopupMenu(e: MouseEvent) {
        val actions = listOf(if (isPaused) "Resume" else "Pause", "Set Timer")

        val popup = JBPopupFactory.getInstance()
            .createPopupChooserBuilder(actions)
            .setTitle("Actions")
            .setItemChosenCallback { selectedValue ->
                if (selectedValue == "Pause" || selectedValue == "Resume") {
                    // Toggle pause state
                    isPaused = !isPaused
                    ApplicationManager.getApplication().invokeLater {
                        statusBar?.updateWidget(ID())
                    }
                } else if (selectedValue == "Set Timer") {
                    setTimerLength()
                }
            }
            .setRequestFocus(true)
            .createPopup()

        // Show the popup at the mouse click location
        // Use the event's component as a fallback if statusBar component is not suitable
        val parentComponent = statusBar?.component as? Component ?: e.component
        popup.showInScreenCoordinates(parentComponent, e.locationOnScreen)
    }

    private fun setTimerLength() {
        val input = Messages.showInputDialog(
            project,
            "Enter new session length in minutes:",
            "Set Session Length",
            Messages.getQuestionIcon(),
            "25",
            null
        )
        if (input != null && input.isNotEmpty()) {
            val newMinutes = input.toIntOrNull()
            if (newMinutes != null && newMinutes > 0) {
                sessionSeconds = newMinutes * 60
                totalSeconds = sessionSeconds
                ApplicationManager.getApplication().invokeLater {
                    statusBar?.updateWidget(ID())
                }
            } else {
                Messages.showErrorDialog(project, "Please enter a valid positive integer.", "Invalid Input")
            }
        }
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        statusBar.updateWidget(ID())
    }

    override fun dispose() {
        timerTask?.cancel(true)
    }

    override fun copy(): StatusBarWidget = SessionCountdownWidget(project)
}