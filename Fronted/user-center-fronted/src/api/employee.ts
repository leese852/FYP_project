import myAxios from "@/request";

export const employeeLogin = async (params: any) => {
  return await myAxios.request({
    url: "/employee/login",
    method: "post",
    data: params,
  });
};

export const getCurrentEmployee = async () => {
  return await myAxios.request({
    url: "/employee/current",
    method: "get",
  });
};

export const employeeLogout = async () => {
  return await myAxios.request({
    url: "/employee/logout",
    method: "post",
  });
};
