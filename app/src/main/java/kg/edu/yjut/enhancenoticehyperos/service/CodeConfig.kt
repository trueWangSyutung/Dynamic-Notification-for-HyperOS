/*
 * Copyright (C) 2024 The LiteNote Project
 * @author OpenAcademic
 * @version 1.0
 * 
 */
package  kg.edu.yjut.enhancenoticehyperos.service

data class CodeConfig(
    var text: String,
    var express : String,
    var type : Int,
    // 0 - 取件码 1 - 验证码
    var from : String
)