package com.raindragon.chapter03_alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raindragon.chapter03_alarm.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // step 0 뷰를 초기화 해주기
        initViews()
        // step 1 데이터 가져오기
        val model = fetchDataFromSharedPreferences()
        renderView(model)
        // step 2 뷰에 데이터 그려주기

    }

    private fun initViews() {
        binding.btnAlarmOnOff.setOnClickListener {
            // 데이터를 확인 한다.
            val model = it.tag as? AlarmDisplayModel
            model ?: return@setOnClickListener

            // 데이터를 저장한다.
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            // 온오프 상태에 따라 작업을 처리한다.
            if (newModel.onOff) {
                // 알람을 등록
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if (before(this)) {
                        add(Calendar.DATE, 1)
                    }
                }
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    ALARM_REQUEST_CODE,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

            } else {
                // 알람을 제거
                cancelAlarm()
            }

        }

        binding.btnAlarmChange.setOnClickListener {
            // calendar 객채로 현재시간을 가져올 수 있다.
            val calendar = Calendar.getInstance()
            // TimePickerDialog 로 시간을 받아온다.
            TimePickerDialog(
                this,
                { picker, hour, minute ->
                    // 데이터를 저장한다.
                    val model = saveAlarmModel(hour, minute, false)
                    // 뷰를 업데이트 한다.
                    renderView(model)

                    // 기존알람을 삭제한다
                    cancelAlarm()

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            ).show()
        }
    }

    private fun saveAlarmModel(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour,
            minute,
            onOff
        )

        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        with(sharedPreference.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.apply {
            tvAmpm.text = model.ampmText
            tvTime.text = model.timeText
            btnAlarmOnOff.text = model.onOffText
            btnAlarmOnOff.tag = model
        }
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreference = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        val timeValue = sharedPreference.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffValue = sharedPreference.getBoolean(ONOFF_KEY, false)
        val alarmData = timeValue.split(":")

        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffValue
        )

        // 보정 및 예외처리

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        if ((pendingIntent == null) and alarmModel.onOff) {
            // 알람은 꺼져있는데, 데이터는 켜져있는 경우
            alarmModel.onOff = false
        } else if ((pendingIntent != null) and alarmModel.onOff.not()) {
            // 알람은 켜져있는데, 데이터는 꺼져있는 경우
            // 알람을 취소한다.
            pendingIntent.cancel()
        }

        return alarmModel
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.cancel()
    }

    companion object {
        const val SHARED_PREFERENCE_NAME = "time"
        const val ALARM_KEY = "alarm"
        const val ONOFF_KEY = "onOff"
        const val ALARM_REQUEST_CODE = 1000
    }
}