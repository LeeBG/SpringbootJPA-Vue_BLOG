<script setup lang="ts">
import { ref } from 'vue'

import axios from 'axios'

// import {reactive, ref} from "vue";
//
//   // 일반 count 변수를 사용하면 갑 증가되지 않는다.
//
//   // 반응형 변수 선언
//   const count = ref(0);
//
//   // Reactive 변수
//   const obj = reactive({count});

const title = ref('')
const content = ref('')

const write = function () {
  axios.post("/api/posts", {
    title: title.value,
    content: content.value
  })
}
</script>

<template>
  <div>
    <el-input v-model="title" type="text" placeholder="제목을 입력해주세요" />
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15" />
  </div>
  <div class="mt-2">
    <el-button type="primary" @click="write()">글 작성완료</el-button>
  </div>
</template>

<style></style>
