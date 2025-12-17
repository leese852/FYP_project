import axios from "axios";
import {message} from "ant-design-vue";
import { errorMessages } from "@vue/compiler-core";

const myAxios = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
    // headers: { "X-Custom-Header": "foobar" },
    //携带cookie
    withCredentials: true,
});

// 添加请求拦截器
myAxios.interceptors.request.use(
    function (config) {
        // 在发送请求之前做些什么
        return config;
    },
    function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 添加响应拦截器
myAxios.interceptors.response.use(
    // 范围内的状态码都会触发该函数。
    function (response) {
        // 对响应数据做点什么
        console.log('API响应:', response);
        const { data } = response;

        // 根据的 BaseResponse 结构：
        // code: 状态码 (0表示成功)
        // data: 真正的业务数据
        // message: 提示信息
        // description: 详细描述

        if (data.code === 40100) {
            if (
                !response.request.responseURL.includes("user/current") &&
                !window.location.pathname.includes("/user/login")
            ) {
                message.warning('请先登录');
                window.location.href = `/user/login?redirect=${window.location.href}`;
            }
            return Promise.reject(new Error(data.message || '未登录'))
        }

        if(data.code !=0 && data.code != 200){
            const errorMsg = data.message || `请求失败，错误码: ${data.code}`;
            message.error(errorMsg);
            return Promise.reject(new Error(errorMsg));
        }
        return data.data;
    },
    function (error) {
        // 超出  范围的状态码都会触发该函数
        // 对响应错误做点什么
        return Promise.reject(error);
    }
);

export default myAxios;
