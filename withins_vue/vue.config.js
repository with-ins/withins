const { defineConfig } = require("@vue/cli-service");
const path = require("path");

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  outputDir: path.resolve(__dirname, "../withins_server/src/main/resources/static"),

  publicPath: '/',
  assetsDir: 'static',

  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src'),
        // CSS에서 사용할 수 있는 폰트 별칭 추가
        '@fonts': path.resolve(__dirname, 'src/fonts')
      }
    }
  },

  chainWebpack: config => {
    // CSS에서 alias 처리를 위한 설정
    config.module
      .rule('css')
      .oneOf('vue-modules')
      .use('css-loader')
      .tap(options => {
        // options의 기본값을 유지하면서 alias 설정 추가
        return {
          ...options,
          import: true,
          // webpack alias가 CSS에서 작동하도록 설정
          url: true
        };
      });

    // SASS/SCSS 파일에서도 alias 사용 가능하도록 설정
    ['vue-modules', 'vue', 'normal-modules', 'normal'].forEach(match => {
      config.module
        .rule('scss')
        .oneOf(match)
        .use('sass-loader')
        .tap(options => {
          return {
            ...options,
            sassOptions: {
              ...options.sassOptions,
              // SASS에서 alias 사용 가능하도록 설정
              includePaths: [
                path.resolve(__dirname, 'src/fonts')
              ]
            }
          };
        });
    });
  }
});