var AWS = require("aws-sdk");

AWS.config.update({
    region: "us-east-2",
    endpoint: "https://dynamodb.us-east-2.amazonaws.com/",
    maxAttempts: 1
});

var dynamodb = new AWS.DynamoDB();

const getDetails = async (tableName) => {
    let params = { TableName: tableName };
    return await dynamodb.describeTable(params).promise();
}

const getBackups = async (tableName) => {
    let params = { TableName: tableName };
    return await dynamodb.listBackups(params).promise();
}
const createBackup = async (bname, tableName) => {
    let params = {
        TableName: tableName,
        BackupName: bname
    };
    return await dynamodb.createBackup(params).promise();
}
const deleteBackup = async (arn) => {
    let params = { BackupArn: arn };
    return await dynamodb.deleteBackup(params).promise();
}

exports.handler = async (event) => {
    let count = 0;

    const response = {
        statusCode: 200,
        count: count,
        body: JSON.stringify('Backup created successfully')
    };
    let backup = await getBackups('Company')
    if (backup && backup.BackupSummaries && backup.BackupSummaries.length > 0) {
        let arn = backup.BackupSummaries[0].BackupArn;
        console.log('Deleting backup with arn ' + arn);
        let deleteResp = await deleteBackup(arn)
        console.log(deleteResp);
    } else {
        console.log(backup);
    }
    let date = new Date();
    let backupName = `${date.getDate()}_${date.getMonth() + 1}_${date.getFullYear()}_Company`;
    let res = await createBackup(backupName, 'Company')
    console.log(res);
    return response;
};