function isValidUsername(str) {
    return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal(path) {
    return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone(val) {
    if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
        return false
    } else {
        return true
    }
}

//校验账号
function checkUserName(rule, value, callback) {
    if (value == "") {
        callback(new Error("Please enter the account"))
    } else if (value.length > 20 || value.length < 3) {
        callback(new Error("The account length should be between 3 and 20 characters"))
    } else {
        callback()
    }
}

//校验姓名
function checkName(rule, value, callback) {
    if (value == "") {
        callback(new Error("Please enter the name"))
    } else if (value.length > 12) {
        callback(new Error("The name length should be between 1 and 12 characters"))
    } else {
        callback()
    }
}

function checkPhone(rule, value, callback) {
    // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
    if (value == "") {
        callback(new Error("Please enter the phone number"))
    } else if (!isCellPhone(value)) {//引入methods中封装的检查手机格式的方法
        callback(new Error("Please enter a valid phone number!"))
    } else {
        callback()
    }
}


function validID(rule, value, callback) {
    // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
    let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
    if (value == '') {
        callback(new Error('Please enter the ID number'))
    } else if (reg.test(value)) {
        callback()
    } else {
        callback(new Error('The ID number should be 15 or 18 digits'))
    }
}