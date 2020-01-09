package com.example.delieverydemo.utils

import java.io.IOException

/**
​
 * Purpose – File contains classes for various exceptions
​
 * @author ​Rishabh Gupta
​
 * Created on January 9, 2020
​
 * Modified on January 9, 2020
 *
 * */

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)