/**
 * Created by administrator on 16/12/4.
 */

const gulp = require('gulp');

const less = require('gulp-less');
const path = require('path');

const sourcemaps = require('gulp-sourcemaps');
const babel = require('gulp-babel');
const concat = require('gulp-concat');
const uglify = require('gulp-uglify');
const replace = require('gulp-replace');
const rename = require("gulp-rename");

gulp.task('less', function () {
  return gulp.src('less/**/style.less')
    .pipe(less({
      paths: [path.join(__dirname, 'less', 'includes')]
    }))
    .pipe(rename({basename: "style"}))
    .pipe(gulp.dest('css'));
});

gulp.task('es', () => {
  return gulp.src([
    'es6/**/require.js',
    'es6/**/message.js',
    'es6/**/http.js',
    'es6/**/fastclick.js',
    //!!以上顺序不得变更
    'es6/**/keyboard.js',
    'es6/**/*.js'
  ]).pipe(sourcemaps.init())
    .pipe(babel({
      presets: ['es2015']
    }))
    .pipe(concat('payment.2.0.1.js'))
    .pipe(uglify())
    .pipe(rename({suffix: ".min"}))
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('js/2.0.1'));
});

gulp.task('replace', function () {
  return gulp.src('WEB-INF/jsp/*.jsp')
    .pipe(replace('0.1.19', '0.1.19'))
    .pipe(gulp.dest('WEB-INF/jsp'));
});

// default 使用默认配置 开发时候使用
gulp.task('build', ['es', 'less', 'replace']);