package com.hiel.hielside.common.utilities.mail

import com.hiel.hielside.common.utilities.LogUtility
import com.hiel.hielside.common.utilities.ProfileUtility
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

@Component
class MailUtility(
    private val profileUtility: ProfileUtility,
    private val javaMailSender: JavaMailSender,

    @Value("\${mail.master-address}")
    private val masterAddress: String,

    @Value("\${mail.test-emails}")
    private val testEmails: List<String>,
) {
    companion object : LogUtility

    fun sendMail(to: String, subject: String, text: String) {
        val msg = SimpleMailMessage().apply {
            from = masterAddress
            setTo(to)
            setSubject(subject)
            setText(text)
        }

        if (profileUtility.isProduction() || testEmails.contains(to)) {
            javaMailSender.send(msg)
        } else {
            log.debug("MailUtility::sendMail (msg={})", msg)
        }
    }

    fun <T : MailTemplateParams> sendMail(to: String, template: MailTemplate<T>) {
        sendMail(
            to = to,
            subject = template.getSubject(),
            text = template.getText(),
        )
    }
}
