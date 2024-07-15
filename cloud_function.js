const axios = require('axios');
exports.pageview = async (req, res) => {
    if (req.body.state === undefined) {
        res.status(400).send('No state is defined!');
    }
    if (req.body.secrets === undefined) {
        res.status(400).send('No secrets is defined!');
    }
    res.header("Content-Type", "application/json");
    if(req.body.setup_test === true){
        res.status(200).send({
        state: {
            cursor: "2023-11-16T08:00:00Z"
        },
        insert: {
            transactions: [ 
            {"_id":id1,"url":light_url,"pageview":1,
            "_dateTime":"2023-11-28T01:12:00+01:00"}, {"_id":id2,"url":url2,"pageview":2,
            "_dateTime":"2023-11-28T01:13:00+01:00"}]
        },
        schema : {
            transactions : {
                primary_key : ['_id']
            }
        },
        hasMore : false
    });
    }else{
        res.status(200).send(await update(req.body.state, req.body.secrets, req.body.customPayload));
    }
};
async function update(state, secrets, payload) {
    // Fetch records using api calls
    let [insertTransactions, newTransactionsCursor] = await apiResponse(state, secrets,payload);
    // Populate records and state
    return ({
        state: {
            cursor: newTransactionsCursor
        },
        insert: {
            transactions: insertTransactions
        },
        schema : {
            transactions : {
                primary_key : ['_id']
            }
        },
        hasMore : false
    });
}
async function apiResponse(state, secrets,payload) {
    let dateFromValue ="";
    if (JSON.stringify(state) === '{}' ){
        dateFromValue =new Date()
        dateFromValue.setMinutes(dateFromValue.getMinutes() - 5);
    }else{
        dateFromValue = new Date(state.cursor);  
    }
    var dateToValue = new Date();
    
    try {
        const authToken = secrets.apiToken;
        const response =  await axios.get('https://eu2.dataapi.kilkaya.com/api/query', {
            params: {
                schemaname: 'pageview',
                datefrom: dateFromValue.toISOString().split('.')[0]+'Z',
                dateto: dateToValue.toISOString().split('.')[0]+'Z',
                columns: 'url,pageview,_dateTime'
            },
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Accept': '*/*',
                'Content-Type': 'application/json'
            }
        });
        var insertTransactions = [];
        response.data.data.forEach((element)=>
            insertTransactions.push(element.attributes))
        var currentTimestamp = new Date(dateToValue).toISOString();
        return [insertTransactions, currentTimestamp];
    } catch (error) {
        console.error(error);
    }
}