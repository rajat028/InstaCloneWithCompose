package com.example.instaclonewithcompose.data.pref

interface InstaClonePref {
    fun get(key: String, defValue: String): String
    
    fun get(key: String, defValue: Int): Int
    
    fun get(key: String, defValue: Boolean): Boolean
    
    fun get(key: String, defValue: Long): Long
    
    fun get(key: String, devValue: Float): Float
    
    fun get(key: String, defaultValue: Double): Double
    
    fun set(key: String, value: String)
    
    fun set(key: String, value: Int)
    
    fun set(key: String, value: Float)
    
    fun set(key: String, value: Boolean)
    
    fun set(key: String, value: Long)
    
    fun set(key: String, value: Double)
    
    fun remove(key: String)
    
    fun clear()
}