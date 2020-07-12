<!doctype html>
<html>
    <head>
        <title>Pass by value</title>
        <style>
            body {
                font-family: sans-serif;
            }
        </style>
    </head>
    <body>
        <h1>Pass by value</h1>
<?php
$anumber = 41;
function inc_echo_me($value) {
    $value++;
    echo '<p>'.$value.'</p>';
}
inc_echo_me($anumber);
echo '<p>'.$anumber.'</p>';

?>

    </body>
</html>
