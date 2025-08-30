module.exports = {
  root: true,
  env: {
    node: true,
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended', // Vue 3 的推荐规则
    '@vue/prettier', // 集成 Prettier
  ],
  rules: {
    'vue/html-indent': ['error', 2], // HTML 缩进 2 空格
    'vue/max-attributes-per-line': [
      'error',
      {
        singleline: 1,
        multiline: 1,
      },
    ],
    'vue/first-attribute-linebreak': [
      'error',
      {
        singleline: 'ignore',
        multiline: 'below',
      },
    ],
    'vue/singleline-html-element-content-newline': 'off', // 关闭单行标签内容换行
  },
}
