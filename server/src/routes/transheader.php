<?php
	use Psr\Http\Message\ResponseInterface as Response;
	use Psr\Http\Message\ServerRequestInterface as Request;

	require '../vendor/autoload.php';
	require_once '../src/classes/DB.php';
	require_once '../src/models/ModelTransHeader.php';


	$app->get('/transheader/{date1}/{date2}', function ($request, $response, $args) {
		try{
			$list = ModelTransHeader::retrieveList("Date(transdate) between '". $args['date1'] . "' and '" . $args['date2']. "'");
	    $json = json_encode($list);
	    $response->getBody()->write($json);
			return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
	    $msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});

	$app->get('/transheader/{id}', function ($request, $response, $args) {
		try{
			$obj = ModelTransHeader::retrieve($args['id']);	    
			$json = json_encode($obj);
			$response->getBody()->write($json);
			return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
			$response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});

	$app->post('/transheader', function ($request, $response) {
		$json = $request->getBody();
		$obj = json_decode($json);
		try{
			ModelTransHeader::saveToDB($obj);
	    $json = json_encode($obj);
	    $response->getBody()->write($json);
	    return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});

	$app->delete('/transheader/{id}', function ($request, $response) {
		$id = $request->getAttribute('id');
		try{
			ModelTransHeader::deleteFromDB($id);
	    return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}

	});
