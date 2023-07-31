const express = require('express')
const {sequelize} = require('./models')
const indexRouter = require('./routes/index')
const app = express()

app.use(express.urlencoded({extended:true}))

// sequelize 설정
sequelize.sync({force : false})
.then(()=>console.log('DB연결성공!'))
.catch((err)=>{console.log(err);});

//routes 연결
app.use('/', indexRouter)

app.set('port', process.env.PORT||8888)
app.listen(app.get('port'), ()=>{
    console.log(app.get('port'), '번 포트에서 서버연결 기다리는중...');
})