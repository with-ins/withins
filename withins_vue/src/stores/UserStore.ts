import { defineStore } from 'pinia';
import { computed, Ref, ref } from "vue";
import { Member } from "@/domain/Member";
import { apiClient, authApi, userApi } from "@/api/ApiClient";

export const useUserStore = defineStore('user', () => {

  const user: Ref<Member | null> = ref(null)
  const login = async (username: string, password: string, successHandler: Function, failureHandler: Function) => {

    await authApi.formLogin(username, password)
      .then(res => {
        if  (res.status === 200) {
          successHandler(res);
        }
      })
      .catch(err => failureHandler(err));
  }

  const logout = async (successHandler: Function, failureHandler: Function) => {

    return await authApi.logout()
      .then(res => {
        user.value = null;
        successHandler(res)
      })
      .catch(err => failureHandler(err));
  }

  const getUserData = async () => {
    await userApi.loadUser()
      .then(res => {
        if (res.status === 200) {
          let data = res.data;
          user.value = new Member(data);
        }
      })
      .catch(err => user.value = null);
  }

  const isAnonymous = computed((): boolean => user.value == null);

  return {user, logout, getUserData, login, isAnonymous};
});