package com.cdr.courseworkdatabase.main.contract

import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.main.home.admin.AllBidsAdmin
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject
import com.cdr.courseworkdatabase.main.home.client.BookRoom

fun Fragment.navigatorMain(): NavigatorMain = requireActivity() as NavigatorMain

interface NavigatorMain {
    // Main-Activity Navigation:
    fun showHomeScreen() // Переход к fragment с доступными действиями (клиент, администратор)
    fun showProfileScreen() // Переход к fragment с информацией о user
    fun signOut() // Переход к fragment с входом/регистрацией (выход из аккаунта)
    fun goBack() // Закрытие текущего fragment

    // SuperAdmin Navigation:
    fun showAddAdminScreen() // Переход к fragment с созданием нового администратора

    // Admin Navigation:
    fun showAllClientsScreen() // Переход к fragment со всеми клиентами в базе данных
    fun showAllBidsAdminScreen() // Переход к fragment со всеми заявками клиентов
    fun showBidInfoAdminScreen(bidInfo: AllBidsAdmin) // Переход к fragment с информацией о выбранной заявке
    fun showBidDamageInfoAdminScreen(bidInfo: AllBidsAdmin) // Переход к fragment с добавлением ущерба
    fun showTopServiceScreen() // Переход к fragment с самым поплуярным сервисом
    fun showStatisticScreen() // Переход к fragment со статистикой

    // Client Navigation:
    fun showAllRoomsScreen() // Переход к fragment со всеми комнатами в базе данных
    fun showInfoRoomScreen(roomId: Long) // Переход к fragment с информацией о выбранной комнате
    fun showTreatmentBidScreen(bidInfo: com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject) // Переход к fragment с ожиданием
    fun showTreatmentBidScreen(bookedRoom: BookRoom) // Переход к fragment с ожиданием
    fun showAllBidsScreen() // Переход к fragment со всеми заявками клиента
    fun showComplaintScreen() // Переход к fragment с составлением и отправки жалобы


}