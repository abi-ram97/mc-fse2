var AWS = require('aws-sdk');
AWS.config.update({region: 'us-east-2'});

var sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const SQS_QUEUE_URL = 'https://sqs.us-east-2.amazonaws.com/495390640421/my-sqs';

const sendMessage = async (msg) => {
    let params = {
        QueueUrl: SQS_QUEUE_URL,
        MessageBody: msg
    };
    return await sqs.sendMessage(params).promise();
}

exports.handler = async (event) => {
    let msg = event.message;
    console.log('Sending message - '+ msg);
    let res = await sendMessage(msg);
    console.log(res);
    
    const response = {
        statusCode: 200,
        body: JSON.stringify('Message sent successfully'),
    };
    return response;
};
