<!doctype html>
<html>
    <head>
        <title>Pass by value verses by reference - array</title>
        <style>
            body {
                font-family: sans-serif;
            }
        </style>
    </head>
    <body>
        <h1>Pass by value verses by reference - array</h1>
        <table>
            <thead><tr><th>Stage</th><th>Data</th><th>Information</th></tr></thead>
            <tbody>
<?php
function display_array(&$array, $prefix) {
    echo '<tr><td>'.$prefix.':</td><td>'.implode(', ', $array).'</td><td></td></tr>';
}

function sumarray_passbyreference(&$sumthis) {
    foreach($sumthis as $key => $value) {
        $sumthis[$key] = $value - 2;
    }
    display_array($sumthis, '$store at end of pass by reference');
    memoryusage('In sumarray_passbyreference');
}

function sumarray_passbyvalue($sumthis) {
    foreach($sumthis as $key => $value) {
        $sumthis[$key] = $value - 2;
    }
    display_array($sumthis, '$store at end of pass by value');
    memoryusage('In sumarray_passbyvalue');
}

function memoryusage($extra) {
    static $lastcurrent = 0;
    static $lastpeak = 0;
    $current = memory_get_usage();
    $peak = memory_get_peak_usage();
    $currentdiff = $current - $lastcurrent;
    $peakdiff = $peak - $lastpeak;
    echo '<tr><td>Memory usage ('.$extra.') - '.date("H:i:s").'</td><td></td><td>Current: '.$current.'('.$currentdiff.') and peak: '.$peak.'('.$peakdiff.')</td></tr>';
    $lastcurrent = $current;
    $lastpeak = $peak;
}

memoryusage('Start');
$store = array();
for($i = 1; $i <= 16; $i++) {
    $store[$i] = pow(2, $i);
}
memoryusage('$store');

display_array($store, '$store at start');
memoryusage('Calling sumarray_passbyvalue');
sumarray_passbyvalue($store);
display_array($store, '$store after sumarray_passbyvalue');
memoryusage('Calling sumarray_passbyreference');
sumarray_passbyreference($store);
display_array($store, '$store after sumarray_passbyreference');
memoryusage('Second call to sumarray_passbyvalue');
sumarray_passbyvalue($store);
display_array($store, '$store after second sumarray_passbyvalue');
memoryusage('Second call to  sumarray_passbyreference');
sumarray_passbyreference($store);
display_array($store, '$store after second sumarray_passbyreference');
memoryusage('End');
?>
            </tbody>
        </table>
    </body>
</html>
