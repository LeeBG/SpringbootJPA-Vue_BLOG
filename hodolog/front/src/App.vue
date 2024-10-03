<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header>
    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/write">글쓰기</RouterLink>
    </nav>
  </header>

  <RouterView />
</template>

<style></style>
