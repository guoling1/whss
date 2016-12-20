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

const hssPath = [
  'es6/hss/*.js'
];

gulp.task('hss', () => {
  return gulp.src(hssPath)
    .pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(rename({suffix: ".min"}))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js/0.1.19'));
});

gulp.task('less-dealer', function () {
  return gulp.src('less/**/dealer.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style"}))
    .pipe(gulp.dest('css'));
});

gulp.task('less-hss', function () {
  return gulp.src('less/**/hss.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style"}))
    .pipe(gulp.dest('css'));
});

gulp.task('replace', function () {
  return gulp.src('WEB-INF/jsp/*.jsp')
    .pipe(replace('0.1.19', '0.1.19'))
    .pipe(gulp.dest('WEB-INF/jsp'));
});

// default 使用默认配置 开发时候使用
gulp.task('build-hss', ['hss', 'less-hss', 'replace']);
gulp.task('build-dealer', ['less-dealer']);