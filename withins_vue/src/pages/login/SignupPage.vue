
<template>
  <div id="signup-wrap">

    <div id="signup">
      <LoadingIndicator
          :show="isLoading"
          type="dots"
      />
      <div class="input-container">
        <div class="input-wrap">
          <h4>아이디</h4>
          <div class="field-inner" :class="{error: usernameErrorMessage.length !== 0}">
            <input class="field"
                   ref="usernameInput"
                   v-model="username"
                   autofocus
                   tabindex="0"
                   minlength="4"
                   maxlength="20"
                   placeholder="4~20자리 / 영문, 숫자"
                   @focusout="validUsername"
                   id="username" name="username" type="text">
          </div>
          <div class="message-wrap" :class="{hidden: usernameErrorMessage.length === 0}">
            <span class="error-message">{{ usernameErrorMessage }}</span>
          </div>
        </div>
        <div class="input-wrap">
          <h4>비밀번호</h4>
          <div class="field-inner" :class="{error: passwordErrorMessage.length !== 0}">
            <input class="field"
                   ref="passwordInput"
                   v-model="password"
                   autofocus
                   tabindex="0"
                   placeholder="8자리 이상 / 영문 대소문자, 숫자, 특수문자 포함"
                   @focusout="validPassword"
                   id="password" name="password" type="password">
          </div>
          <div class="message-wrap" :class="{hidden: passwordErrorMessage.length === 0}">
            <span class="error-message">{{ passwordErrorMessage }}</span>
          </div>
        </div>
        <div class="input-wrap">
          <h4>비밀번호 확인</h4>
          <div class="field-inner" :class="{error: passwordCheckErrorMessage.length !== 0}">
            <input class="field"
                   ref="passwordCheckInput"
                   v-model="passwordCheck"
                   autofocus
                   tabindex="0"
                   placeholder="8자리 이상 / 영문 대소문자, 숫자, 특수문자 포함"
                   @focusout="validPasswordCheck"
                   id="passwordCheck" name="passwordCheck" type="password">
          </div>
          <div class="message-wrap" :class="{hidden: passwordCheckErrorMessage.length === 0}">
            <span class="error-message">{{ passwordCheckErrorMessage }}</span>
          </div>
        </div>
        <div class="input-wrap">
          <h4>이메일</h4>
          <div class="field-inner" :class="{error: emailErrorMessage.length !== 0}">
            <input class="field"
                   ref="emailInput"
                   v-model="email"
                   autofocus
                   tabindex="0"
                   placeholder="example@google.com"
                   @focusout="validEmail"
                   id="email" name="email" type="email">
          </div>
          <div class="message-wrap">
          <span class="info-message">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
              <path d="M13.2432 7.73926C13.1787 6.44181 12.6348 5.21102 11.7119 4.28809C10.7273 3.30352 9.39239 2.75 8 2.75C7.31068 2.75 6.62808 2.88567 5.99121 3.14941C5.35425 3.41325 4.77559 3.80058 4.28809 4.28809C3.80058 4.77559 3.41325 5.35425 3.14941 5.99121C2.88567 6.62808 2.75 7.31068 2.75 8L2.75684 8.25781C2.7864 8.85905 2.9186 9.45145 3.14941 10.0088C3.41325 10.6457 3.80058 11.2244 4.28809 11.7119C4.77559 12.1994 5.35425 12.5867 5.99121 12.8506C6.62808 13.1143 7.31068 13.25 8 13.25C9.39239 13.25 10.7273 12.6965 11.7119 11.7119C12.6965 10.7273 13.25 9.39239 13.25 8L13.2432 7.73926ZM7.25 10.667V7.33301C7.25018 6.91894 7.5859 6.58301 8 6.58301C8.4141 6.58301 8.74982 6.91894 8.75 7.33301V10.667C8.74982 11.0811 8.41411 11.417 8 11.417C7.58589 11.417 7.25018 11.0811 7.25 10.667ZM8.0332 4.58301C8.44731 4.58301 8.78303 4.91894 8.7832 5.33301V5.40039C8.78299 5.81442 8.44729 6.15039 8.0332 6.15039H7.9668C7.55271 6.15039 7.21701 5.81442 7.2168 5.40039V5.33301C7.21697 4.91894 7.55269 4.58301 7.9668 4.58301H8.0332ZM14.7422 8.33496C14.6594 10.0032 13.9601 11.5867 12.7734 12.7734C11.5076 14.0393 9.79021 14.75 8 14.75C7.11363 14.75 6.2359 14.5755 5.41699 14.2363C4.59804 13.8971 3.85336 13.4002 3.22656 12.7734C2.59977 12.1466 2.10289 11.402 1.76367 10.583C1.42449 9.7641 1.25 8.88637 1.25 8C1.25 7.11363 1.42449 6.2359 1.76367 5.41699C2.10289 4.59804 2.59977 3.85336 3.22656 3.22656C3.85336 2.59977 4.59804 2.10289 5.41699 1.76367C6.2359 1.42449 7.11363 1.25 8 1.25C9.79021 1.25 11.5076 1.96069 12.7734 3.22656C14.0393 4.49243 14.75 6.20979 14.75 8L14.7422 8.33496Z" fill="#777984"/>
            </svg>
            계정 분실 시 인증번호를 받을 때 필요해요
          </span>
            <span class="error-message" :class="{hidden: emailErrorMessage.length === 0}">{{ emailErrorMessage }}</span>
          </div>
        </div>
      </div>
      <button type="submit" id="submit" @click="submit" :disabled="formDisabled">회원가입 완료</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from "vue";
import {signupApi} from "@/api/ApiClient";
import LoadingIndicator from "@/components/LoadingIndicator.vue";

const isLoading = ref(false);

const username = ref('');
const password = ref('');
const passwordCheck = ref('');
const email = ref('');

const usernameInput = ref<HTMLInputElement>();
const passwordInput = ref<HTMLInputElement>();
const passwordCheckInput = ref<HTMLInputElement>();
const emailInput = ref<HTMLInputElement>();

const usernameErrorMessage = ref('');
const passwordErrorMessage = ref('');
const passwordCheckErrorMessage = ref('');
const emailErrorMessage = ref('');

const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

async function submit() {
  if (isLoading.value) {
    return;
  }
  isLoading.value = true;
  const isValid = await valid();
  if (!isValid) {
    return
  }
  const result = await signupApi.signup(
      username.value,
      password.value,
      email.value,
  );
  await delay(1000);
  if (result.status === 200) {

  } else if (result.status === 400) {
    for (const [field, message] of Object.entries(result.data)) {
      if (field === 'username') {
        usernameErrorMessage.value = String(message);
      } else if (field === 'password') {
        passwordErrorMessage.value = String(message);
      } else if (field === 'email') {
        emailErrorMessage.value = String(message);
      }
    }
  }
  isLoading.value = false;
}
const formDisabled = computed(() : boolean => {
  return isLoading.value ||
      username.value.length === 0 ||
      password.value.length === 0 ||
      passwordCheck.value.length === 0 ||
      email.value.length === 0;
});
async function valid() : Promise<boolean> {
  return await validEmail() &&
      validPassword() &&
      validPasswordCheck() &&
      await validEmail();
}

async function validUsername() : Promise<boolean> {
  if (username.value.length < 4 || username.value.length > 20) {
    usernameErrorMessage.value = '4 ~ 20자의 영문,숫자만 사용가능합니다.';
    return false;
  }
  if (!/^[a-zA-Z0-9]+$/.test(username.value)) {
    usernameErrorMessage.value = '특수문자는 사용할 수 없습니다.';
    return false;
  }
  const exists : boolean | null = await signupApi.existsUsername(username.value)
  console.log(exists)
  if (exists == null) {
    usernameErrorMessage.value = '서버를 찾을 수 없습니다. 다시 시도해주세요.'
    return false;
  }
  if (exists) {
    usernameErrorMessage.value = '이미 사용중인 아이디입니다.';
    return false;
  }
  usernameErrorMessage.value = '';
  return true;
}
function validPassword() : boolean {
  if (password.value.length < 8) {
    passwordErrorMessage.value = '8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.';
    return false;
  }
  // 영문 대문자 포함 여부
  if (!/[A-Z]/.test(password.value) ||
      !/[a-z]/.test(password.value) ||
      !/[0-9]/.test(password.value) ||
      !/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password.value)
  ) {
    passwordErrorMessage.value = '8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.';
    return false;
  }

  passwordErrorMessage.value = '';
  return true;
}
function validPasswordCheck() : boolean {
  if (password.value !== passwordCheck.value) {
    passwordCheckErrorMessage.value = '비밀번호와 일치하지 않습니다.';
    return false;
  }
  if (passwordCheck.value.length < 8) {
    passwordCheckErrorMessage.value = '8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.';
    return false;
  }
  // 영문 대문자 포함 여부
  if (!/[A-Z]/.test(passwordCheck.value) ||
      !/[a-z]/.test(passwordCheck.value) ||
      !/[0-9]/.test(passwordCheck.value) ||
      !/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(passwordCheck.value)
  ) {
    passwordCheckErrorMessage.value = '8자리 이상 영문 대소문자, 숫자, 특수문자를 조합해주세요.';
    return false;
  }

  passwordCheckErrorMessage.value = '';
  return true;
}

async function validEmail() : Promise<boolean> {
  if (email.value.length === 0) {
    emailErrorMessage.value = '이메일을 입력해주세요.';
    return false;
  }
  // 이메일 형식 검증
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailRegex.test(email.value)) {
    emailErrorMessage.value = '올바른 이메일 형식이 아닙니다.';
    return false;
  }

  const exists : boolean | null = await signupApi.existsEmail(email.value);
  if (exists == null) {
    emailErrorMessage.value = '서버를 찾을 수 없습니다. 다시 시도해주세요.'
    return false;
  }
  if (exists) {
    emailErrorMessage.value = '이미 등록된 이메일입니다.';
    return false;
  }

  emailErrorMessage.value = '';
  return true;
}
</script>
<style scoped>
#signup-wrap {
  width: 100%;
  min-height: calc(100vh - 80px);
  padding: 100px 0;
  display: flex;
  align-items: center;
  justify-content: center;
}
#signup {
  width: 400px;
  border-radius: 16px;
  border: 1px #eee solid;
  box-shadow: 10px 10px 30px rgba(0,0,0,0.06);
  position: relative;
  padding: 35px 40px;

  display: flex;
  flex-direction: column;
  gap: 40px;
}

.input-container {
  display: flex;
  flex-direction: column;
  gap: 21px;
}

.input-wrap > h4 {
  margin-bottom: 8px;
}

.field-inner {
  height: 50px;
  border: 1px #eeeeee solid;
  border-radius: 16px;
  padding: 0 20px;
}
.field-inner.error {
  border: 1px #FF4B4B solid;
}

.field {
  width: 100%;
  height: 100%;
}

.message-wrap {
  margin-left: 8px;
  margin-top: 8px;
  font-size: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.info-message {
  color: var(--f3);
  display: flex;
  align-items: center;
  justify-content: start;
  gap: 8px;
}
.error-message {
  color: #FF4B4B;
}

#submit {
  height: 50px;
  border-radius: 16px;
  background-color: var(--main-color-1);
  color: white;
  font-size: 16px;
}
button:disabled {
  background-color: #999999 !important;
  cursor: not-allowed;
}

.hidden {
  display: none !important;
}
</style>