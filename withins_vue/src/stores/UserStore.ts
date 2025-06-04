import { defineStore } from 'pinia';
import { computed, Ref, ref } from "vue";
import { User } from "@/domain/User";
import { apiClient, authApi, userApi } from "@/api/ApiClient";

export const useUserStore = defineStore('user', () => {
    const user: Ref<User | null> = ref(null)
    const isInitialized = ref(false)

    const initialize = async () => {
        try {
            await loadUserData()
        } finally {
            isInitialized.value = true
        }
    }

    const login = async (username: string, password: string, successHandler: Function, failureHandler: Function) => {
        await authApi.formLogin(username, password)
            .then(async (res) => {
                if (res.status === 200) {
                    await loadUserData();
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

    const loadUserData = async () => {
        await userApi.loadUser()
            .then(res => {
                if (res.status === 200) {
                    let data = res.data;
                    user.value = new User(data);
                }
            })
            .catch(err => {
                user.value = null;
            });
    }

    const isAnonymous = computed((): boolean => user.value == null);
    const isAdmin = computed((): boolean => user.value?.role === 'ADMIN');

    return {
        user,
        logout,
        loadUserData,
        login,
        isAnonymous,
        isAdmin,
        initialize,
        isInitialized
    };
});