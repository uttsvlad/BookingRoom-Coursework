package com.cdr.courseworkdatabase.model

import com.cdr.courseworkdatabase.main.home.admin.*
import com.cdr.courseworkdatabase.main.home.client.*
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject
import com.cdr.courseworkdatabase.main.home.superAdmin.Administrator
import com.cdr.courseworkdatabase.main.home.superAdmin.NewAdministratorDTO
import com.cdr.courseworkdatabase.main.home.superAdmin.StatisticRequest
import com.cdr.courseworkdatabase.main.home.superAdmin.StatisticResponse
import com.cdr.courseworkdatabase.model.authentication.AuthenticationResponse
import com.cdr.courseworkdatabase.model.authentication.RegistrationDTO
import com.cdr.courseworkdatabase.model.authentication.RegistrationResponse
import com.cdr.courseworkdatabase.model.authentication.UserDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface API {
    // Регистрация нового пользователя:
    @POST("registration")
    fun createUser(@Body user: RegistrationDTO?): Call<RegistrationResponse?>?

    // Аутентификация пользователя:
    @POST("login")
    fun checkUser(@Body user: UserDTO?): Call<AuthenticationResponse?>?

    // Получение все клиентов:
    @GET("administrator/showClients")
    fun getAllClients(@Header("Authorization") jwtToken: String): Call<List<AllClientObject>>

    // Получение всех комнат:
    @GET("client/showAllRooms")
    fun getAllRooms(@Header("Authorization") jwtToken: String): Call<List<AllRoomsObject>>

    // Получение всех заявок:
    @GET("client/{username}/getBids")
    fun getAllBids(
        @Header("Authorization") jwtToken: String,
        @Path("username") username: String
    ): Call<List<AllBidsAdminObject>>

    // Получение всех заявок для администратора:
    @GET("administrator/showBids")
    fun getAllBidsAdmin(@Header("Authorization") jwtToken: String): Call<List<AllBidsAdmin>>

    // Отправка результата админа о заявке (принят или отказ):
    @POST("administrator/handleBid")
    fun sendHandleBid(
        @Header("Authorization") jwtToken: String,
        @Body handleBid: AdminBidHandleRequest
    ): Call<ResponseBody>

    // Отправка суммы о ущербе комнаты:
    @POST("administrator/addDamage")
    fun sendDamageSum(
        @Header("Authorization") jwtToken: String,
        @Body damageBid: AddDamageToRegistrationRequest
    ): Call<ResponseBody>

    // Получение информации о самом популярном сервисе:
    @GET("administrator/topService")
    fun getTopService(@Header("Authorization") jwtToken: String): Call<TopService>

    // Получение информации о выбранной комнате:
    @GET("client/showRoomInfo/{id}")
    fun getRoomInfo(
        @Header("Authorization") jwtToken: String,
        @Path("id") id: Long
    ): Call<RoomInfoObject>

    // Отправка заявления о бронировании комнаты:
    @POST("client/sendRoomRegistration")
    fun sendBookRoom(
        @Header("Authorization") jwtToken: String,
        @Body bookRoom: BookRoom
    ): Call<ResponseBody>

    // Отправка жалобы:
    @POST("client/createComplaint")
    fun createComplaint(
        @Header("Authorization") jwtToken: String,
        @Body complaint: ComplaintRequest
    ): Call<ResponseBody>

    // Создание администратора:
    @POST("superAdmin/createAdministrator")
    fun createAdministrator(
        @Header("Authorization") jwtToken: String,
        @Body administrator: NewAdministratorDTO
    ): Call<ResponseBody>

    // Получение все администраторов для списка (экран статистики):
    @GET("superAdmin/getAllAdmins")
    fun getAllAdmins(
        @Header("Authorization") jwtToken: String
    ): Call<List<Administrator>>

    // Получение ссылки для отображения статистики:
    @POST("superAdmin/showStatistic")
    fun getStatistic(
        @Header("Authorization") jwtToken: String,
        @Body statisticInfo: StatisticRequest
    ): Call<StatisticResponse>
}