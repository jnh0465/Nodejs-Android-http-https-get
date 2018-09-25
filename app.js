// --> npm install express --save

var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var fs = require('fs');

app.get('/payment_toapp', (req, res) => {		       	  //// 어플로(어플내에서 저장예정 )
  /*
  var data =[
  {'paymentNum':dynamodb.paymentNum_list,'totPrice':dynamodb.totPrice_list,'date':dynamodb.date_list,
'menu_list':dynamodb.menu_list, 'obj_length':dynamodb.obj_length, 'obj_orderInfo_length' : dynamodb.obj_orderInfo_length}
];
*/
  var data =[
    {'paymentNum':'122f6c3a-574a-4e56-80ca-b4f6ea1b20e05',
    'totPrice':'5800','date':'2018-09-23 15:10',
  'menu_list':'aide', 'obj_length':1, 'obj_orderInfo_length' : 6}
  ];

  console.log(data);
  res.json(data);
});

////////////////////////////////////////////////////////////////////////////////////////////////////////
app.listen(3000, function(){
  console.log("Express server has started on port 3000");
});
