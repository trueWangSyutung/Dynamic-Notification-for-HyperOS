package kg.edu.yjut.litenote.miuiStringToast.devicesSDK

/*
  * This file is part of HyperCeiler.

  * HyperCeiler is free software: you can redistribute it and/or modify
  * it under the terms of the GNU Affero General Public License as
  * published by the Free Software Foundation, either version 3 of the
  * License.

  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU Affero General Public License for more details.

  * You should have received a copy of the GNU Affero General Public License
  * along with this program.  If not, see <https://www.gnu.org/licenses/>.

  * Copyright (C) 2023-2024 HyperCeiler Contributions
*/
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log


@SuppressLint("PrivateApi")
object PropUtils {
    private val TAG: String =" ITAG.TAG"
    fun getProp(context: Context, name: String): String {
        return try {
            classLoaderMethod(context, name)
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils classLoader getProp String", e)
            ""
        }
    }

    fun getProp(name: String?, def: Boolean): Boolean {
        return try {
            val cls = Class.forName("android.os.SystemProperties")
            java.lang.Boolean.TRUE == invokeMethod<Any>(
                cls, "getBoolean", arrayOf(
                    String::class.java,
                    Boolean::class.javaPrimitiveType
                ), name!!, def
            )
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils getProp int", e)
            false
        }
    }

    fun getProp(name: String?, def: Int): Int {
        return try {
            val cls = Class.forName("android.os.SystemProperties")
            invokeMethod(
                cls, "getInt", arrayOf(
                    String::class.java,
                    Int::class.javaPrimitiveType
                ), name!!, def
            )
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils getProp int", e)
            0
        }
    }

    fun getProp(name: String?, def: Long): Long {
        return try {
            val cls = Class.forName("android.os.SystemProperties")
            invokeMethod(
                cls, "getLong", arrayOf(
                    String::class.java,
                    Long::class.javaPrimitiveType
                ), name!!, def
            )
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils getProp long", e)
            0L
        }
    }

    fun getProp(name: String?, def: String?): String {
        return try {
            invokeMethod(
                Class.forName("android.os.SystemProperties"),
                "get", arrayOf(String::class.java, String::class.java),
                name!!, def!!
            )
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils getProp String", e)
            ""
        }
    }

    fun getProp(name: String?): String {
        return try {
            invokeMethod(
                Class.forName("android.os.SystemProperties"),
                "get", arrayOf(String::class.java), name!!
            )
        } catch (e: Throwable) {
            Log.e(TAG, "PropUtils getProp String no def", e)
            ""
        }
    }



    @Throws(Throwable::class)
    private fun classLoaderMethod(context: Context, name: String): String {
        val classLoader = context.classLoader
        val cls = classLoader.loadClass("android.os.SystemProperties")
        val method = cls.getDeclaredMethod("get", String::class.java)
        method.isAccessible = true
        return method.invoke(cls, name) as String
    }

    /**
     * @noinspection unchecked
     */
    @Throws(Throwable::class)
    private fun <T> invokeMethod(
        cls: Class<*>,
        str: String,
        clsArr: Array<Class<*>?>,
        vararg objArr: Any
    ): T {
        val declaredMethod = cls.getDeclaredMethod(str, *clsArr)
        declaredMethod.isAccessible = true
        return declaredMethod.invoke(null, *objArr) as T
    }
}