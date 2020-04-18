<?php
	use Psr\Http\Message\ResponseInterface as Response;
	use Psr\Http\Message\ServerRequestInterface as Request;

	require '../vendor/autoload.php';
	require_once '../src/classes/DB.php';
	require_once '../src/models/ModelItem.php';


	$app->get('/item', function ($request, $response, $args) {
		try{
			$sql = "select * from item";
	    $data = DB::openQuery($sql);
	    $json = json_encode($data);
	    $response->getBody()->write($json);

			return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
	    $msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}
	});

	$app->post('/item', function ($request, $response) {
		$json = $request->getBody();
		// $projectcode = $request->getAttribute('projectcode');
		$obj = json_decode($json);
		try{
			ModelItem::saveToDB($obj);
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

	$app->delete('/item/{id}', function ($request, $response) {
		$id = $request->getAttribute('id');
		try{
			ModelItem::deleteFromDB($id);
	    return $response->withHeader('Content-Type', 'application/json;charset=utf-8');
		}catch(Exception $e){
			$msg = $e->getMessage();
	    $response->getBody()->write($msg);
			return $response->withStatus(500)
				->withHeader('Content-Type', 'text/html');
		}

	});
