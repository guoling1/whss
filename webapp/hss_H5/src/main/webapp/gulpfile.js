/**
 * Created by administrator on 16/12/4.
 */

const gulp = require('gulp');

const less = require('gulp-less');
const path = require('path');

const sourcemaps = require('gulp-sourcemaps');
const babel = require('gulp-babel');
const concat = require('gulp-concat');
const replace = require('gulp-replace');
const rename = require("gulp-rename");
const px2rem = require('gulp-px2rem');

gulp.task('js-dealer', () => {
  return gulp.src('es6/dealer/*.js')
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(rename({suffix: ".min"}))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js/dealer/0.1.1'));
});

gulp.task('less-dealer', function () {
  return gulp.src('less/**/dealer.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style"}))
    .pipe(gulp.dest('css'));
});

gulp.task('replace-dealer', function () {
  return gulp.src('WEB-INF/jsp/dealer/*.jsp')
    .pipe(replace('0.1.1', '0.1.1'))
    .pipe(gulp.dest('WEB-INF/jsp/dealer'));
});

gulp.task('less-hss', function () {
  return gulp.src('less/**/hss.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style.2.2.14"}))
    .pipe(gulp.dest('css'));
});

gulp.task('replace-hss-calc', ['build-hss'], function () {
  return gulp.src('css/hss/style.2.2.14.css')
    .pipe(replace('calc(0%)', 'calc(100% - 100px)'))
    .pipe(replace('calc(38%)', 'calc(100% - 62px)'))
    .pipe(gulp.dest('css/hss'));
});


const px2remOptions = {
  rootValue: 37.5,
  replace: true,
  minPx: 1
};
const postCssOptions = {
  map: true
};
gulp.task('less-hss-beta', function () {
  return gulp.src('less/**/hss.2.0.2.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(px2rem(px2remOptions, postCssOptions))
    .pipe(rename({basename: "style.2.0.3"}))
    .pipe(gulp.dest('css'));
});

gulp.task('js-hss', () => {
  return gulp.src('es6/hss/*.js')
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(rename({suffix: ".min"}))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js/hss/2.2.21'));
});

gulp.task('replace-hss', function () {
  return gulp.src('WEB-INF/jsp/*.jsp')
    .pipe(replace('vendor.1.0.9.9.min.js', 'vendor.1.0.9.10.min.js'))
    .pipe(replace('style.2.2.13.css', 'style.2.2.14.css'))
    .pipe(replace('style.2.0.2.css', 'style.2.0.3.css'))
    .pipe(replace('/hss/2.2.20/', '/hss/2.2.21/'))
    .pipe(gulp.dest('WEB-INF/jsp'));
});

// default 使用默认配置 开发时候使用
gulp.task('build-hss', ['js-hss', 'less-hss', 'less-hss-beta', 'replace-hss']);
gulp.task('build-dealer', ['js-dealer', 'less-dealer', 'replace-dealer']);