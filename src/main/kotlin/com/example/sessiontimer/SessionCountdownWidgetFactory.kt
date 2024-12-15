package com.example.sessiontimer

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class SessionCountdownWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = "com.example.sessiontimer.widget"
    override fun getDisplayName(): String = "Session Countdown Timer"
    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return SessionCountdownWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
