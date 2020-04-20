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

	$app->get('/transheader/{warehouse_id}/{yearperiod}/{monthperiod}', function ($request, $response, $args) {
		try{
			$monthperiod = 0;
			$yearperiod = 0;
			$warehouse_id = 0;
			if (isset($args['monthperiod'])) $monthperiod = $args['monthperiod'];
			if (isset($args['yearperiod'])) $yearperiod = $args['yearperiod'];
			if (isset($args['warehouse_id'])) $warehouse_id = $args['warehouse_id'];
			$sql = "select * from transheader where month(transdate) = ".$monthperiod." and year(transdate) = " . $yearperiod;
			if ($warehouse_id > 0) $sql = $sql . " and warehouse_id = ". $warehouse_id;

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

	$app->get('/sellingqty/{yearperiod}/{monthperiod}', function ($request, $response, $args) {
		try{
			$monthperiod = 0;
			$yearperiod = 0;

			if (isset($args['monthperiod'])) $monthperiod = $args['monthperiod'];
			if (isset($args['yearperiod'])) $yearperiod = $args['yearperiod'];
			$sql = "select b.itemname, c.warehousename, sum(abs(a.qty)) as qty
							from transdetail a
							inner join item b on a.item_id = b.id
							inner join warehouse c on a.warehouse_id = c.id
							where a.header_flag = 2
							and month(transdate) = ".$monthperiod." and year(transdate) = " . $yearperiod ."
							group by b.itemname, c.warehousename
							order by b.itemname, c.id";
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
