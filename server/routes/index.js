const express = require('express')
const router = express.Router()

router.get("/d", (req,res) => {
    console.log("abcdf")
})

module.exports = router