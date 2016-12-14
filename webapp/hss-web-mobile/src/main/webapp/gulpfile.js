/**
 * Created by administrator on 16/12/4.
 */

const gulp = require('gulp');

const less = require('gulp-less');
const path = require('path');

const sourcemaps = require('gulp-sourcemaps');
const babel = require('gulp-babel');
const concat = require('gulp-concat');

const shoesPath = [
  'es6/**/require.js',
  'es6/shoes/tools.js',
  'es6/shoes/message.js',
  'es6/shoes/http.js',
  'es6/shoes/validate.js',
  'es6/shoes/fastclick.js',
  'es6/shoes/*.js'
];

const pagesPath = [
  'es6/pages/*.js'
];

gulp.task('shoes', () => {
  return gulp.src(shoesPath)
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(concat('shoes.js'))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js'));
});

gulp.task('pages', () => {
  return gulp.src(pagesPath)
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js'));
});

gulp.task('less', function () {
  return gulp.src('less/**/*.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(gulp.dest('css'));
});

// default 使用默认配置 开发时候使用
gulp.task('default', ['shoes', 'pages', 'less']);