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

gulp.task('less-hsy', function () {
  return gulp.src('less/**/hsy.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style"}))
    .pipe(gulp.dest('css'));
});

gulp.task('js-hsy', () => {
  return gulp.src('es6/hsy/*.js')
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(rename({suffix: ".min"}))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js/hsy/2.0.1'));
});

gulp.task('replace-hsy', function () {
  return gulp.src('WEB-INF/jsp/*.jsp')
    .pipe(replace('2.0.1', '2.0.1'))
    .pipe(gulp.dest('WEB-INF/jsp'));
});

// default 使用默认配置 开发时候使用
gulp.task('build-hsy', ['js-hsy', 'less-hsy', 'replace-hsy']);
gulp.task('build-dealer', ['js-dealer', 'less-dealer', 'replace-dealer']);