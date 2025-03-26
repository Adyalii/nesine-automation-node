const validateRequest = (schema) => {
    return (req, res, next) => {
      const { error } = schema.validate(req.body);
      if (error) {
        return res.status(400).json({
          statusCode: 400,
          error: error.details[0].message
        });
      }
      next();
    };
  };
  
  module.exports = validateRequest;