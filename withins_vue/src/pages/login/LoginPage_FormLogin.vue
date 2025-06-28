
<template>
  <div id="login-wrap">
    <div id="login">
      <LoadingIndicator
        :show="isLoading"
        type="dots"
      />

      <form @submit.prevent="submit">
        <div class="form-wrap">
          <div class="field_wrap">
            <div class="field_inner">
              <label for="username">
                <input class="field"
                       ref="usernameInput"
                       v-model="username"
                       autofocus
                       tabindex="0"
                       placeholder="아이디"
                       autocomplete="off"
                       id="username" name="username" type="text">
              </label>
            </div>
            <div class="field_inner">
              <label for="password">
                <input class="field"
                       ref="passwordInput"
                       v-model="password"
                       tabindex="0"
                       placeholder="비밀번호"
                       autocomplete="off"
                       id="password" name="password" type="password">
              </label>
            </div>
          </div>
          <button type="submit" id="submit" :disabled="formDisabled">로그인</button>
        </div>
        <div class="form-menu">
          <RouterLink to="/find">아이디/비밀번호 찾기</RouterLink>
          <RouterLink to="/signup">회원가입</RouterLink>
        </div>
      </form>

      <div id="form">
        <a href="/oauth2/authorization/kakao" id="kakao-btn" class="social-btn">
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M9.98613 0.90918C4.47157 0.90918 0 4.28547 0 8.45999C0 11.038 1.71984 13.3141 4.34398 14.6732L3.29543 18.4486C3.29543 18.4486 3.12344 18.8208 3.39528 18.988C3.47157 19.0543 3.57033 19.091 3.67268 19.091C3.77502 19.091 3.87378 19.0543 3.95007 18.988L8.74897 15.8975C9.16506 15.9353 9.58113 15.9622 10.0139 15.9622C15.5284 15.9622 20 12.5913 20 8.41143C20 4.23152 15.5007 0.90918 9.98613 0.90918Z" fill="#3C1E1E"/>
          </svg>
          <span>카카오톡으로 로그인</span>
        </a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import { computed, ref } from "vue";
import { useUserStore } from "@/stores/UserStore";
import LoadingIndicator from "@/components/LoadingIndicator.vue";
import { useRouter } from "vue-router";

const username = ref('');
const password = ref('');
const usernameInput = ref<HTMLInputElement>();
const passwordInput = ref<HTMLInputElement>();
const isLoading = ref(false);

const userStore = useUserStore();
const { login } = userStore;

const formDisabled = computed(() : boolean => {
  return isLoading.value || username.value.length === 0 || password.value.length === 0;
});

const router = useRouter();
const submit = async () => {
  isLoading.value = true;
  await login(
    username.value, password.value,
    (res: any) => {
      console.log('로그인 성공');
      router.push('/');
    },
    (res: any) => {
      const message = res.response?.data?.message;
      if (message) {
        alert(message)
      }
      passwordInput.value?.focus();
    }
  );
  isLoading.value = false;
}

</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.form-wrap {
  display: flex;
  flex-direction: column;
  gap: 27px;
}
.form-menu {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}
.form-menu a {
  color: var(--f3);
  font-weight: 300;
}
.form-menu a:hover {
  text-decoration: underline;
}
.field_wrap {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.field_inner {
  height: 50px;
  border: 1px #eeeeee solid;
  border-radius: 16px;
  padding: 0 20px;
}

.field {
  width: 100%;
  height: 100%;
}

#submit {
  height: 50px;
  border-radius: 16px;
  background-color: var(--main-color-1);
  color: white;
  font-size: 16px;
}

#login-wrap {
  position: relative;
  width: 100%;
  min-height: calc(100vh - 80px);

  display: flex;
  align-items: center;
  justify-content: center;
}
#login {
  width: 400px;
  border-radius: 16px;
  border: 1px #eee solid;
  box-shadow: 10px 10px 30px rgba(0,0,0,0.06);
  position: relative;
  padding: 45px 30px;

  display: flex;
  flex-direction: column;
  gap: 61px;
}

.social-btn {
  width: 100%;
  height: 50px;
  border-radius: 16px;
  font-size: 1rem;
  display: flex;
  align-items: center;
  background-color: #FEE500;
  position: relative;
  padding: 0 20px;
}
.social-btn span {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: #3C1E1E;
}
#kakao-btn:hover {
  background-color: #fbe006;
}

button:disabled {
  background-color: #999999 !important;
  cursor: not-allowed;
}

#form {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}
</style>