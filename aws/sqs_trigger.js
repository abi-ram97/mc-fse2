exports.handler = async function(event, context) {
  console.log(event);
  const response = {
        statusCode: 200,
        body: JSON.stringify('Message received successfully')
    };
  
  event.Records.forEach(record => {
    const { body } = record;
    console.log(body);
  });
  return response;
}