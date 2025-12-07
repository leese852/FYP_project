import myAxios from "@/request";

// 用户注册接口
export const userRegister = async (params: any) => {
  return await myAxios.request({
    url: "/user/register",
    method: "post",
    data: params,
  });
};

// 用户登录接口
export const userLogin = async (params: any) => {
  return await myAxios.request({
    url: "/user/login",
    method: "post",
    data: params,
  });
};

// 获取用户信息接口
export const getCurrentUser = async () => {
  return await myAxios.request({
    url: "/user/current",
    method: "get",
  });
};

//获取用户列表
export const getUserList = async (username: any) => {
  return await myAxios.request({
    url: "/user/search",
    method: "get",
    params: { username },
  });
};

export const logout = async () => {
  return await myAxios.request({
    url: "/user/logout",
    method: "post",
  });
};

// 删除用户接口
export const deleteUser = async (id: string) => {
  return await myAxios.request({
    url: `/api/user/delete`,
    method: "post",
    data: id,
    //因为在后端使用了@RequestBody，因此还需要指定下面的传输类型
    headers: {
      "Content-Type": "application/json",
    },
  });
};
