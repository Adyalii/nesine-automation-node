const express = require('express');
const router = express.Router();
const CredentialsController = require('../controllers/CredentialsController');

router.get('/getCredentials', CredentialsController.getCredentials);

module.exports = router;