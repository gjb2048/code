<!doctype html>
<html>
    <head>
        <title>Pass by value verses by reference - class</title>
        <style>
            body {
                font-family: sans-serif;
            }
        </style>
    </head>
    <body>
        <h1>Pass by value verses by reference - class</h1>
        <table>
            <thead><tr><th>Stage</th><th>Data</th><th>Information</th></tr></thead>
            <tbody>
<?php
function display_class(&$class, $prefix) {
    $classasarray = array();
    for($i = 1; $i <= 16; $i++) {
        $classattr = 'val'.$i;
        $classasarray[] = $class->$classattr;
    }
    echo '<tr><td>'.$prefix.':</td><td>'.$class->name.': '.implode(', ', $classasarray).'</td><td></td></tr>';
}

function sumclassval_passbyreference(&$sumthis) {
    for($i = 1; $i <= 16; $i++) {
        $classattr = 'val'.$i;
        $sumthis->$classattr = $sumthis->$classattr - 2;
    }
    display_class($sumthis, '$store at end of pass by reference');
    memoryusage('In sumarray_passbyreference');
}

function sumclassval_passbyvalue($sumthis) {
    for($i = 1; $i <= 16; $i++) {
        $classattr = 'val'.$i;
        $sumthis->$classattr = $sumthis->$classattr - 2;
    }
    display_class($sumthis, '$store at end of pass by value');
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
class myStore {
    public $name;
    public $val1, $val2, $val3, $val4, $val5, $val6, $val7, $val8,
        $val9, $val10, $val11, $val12, $val13, $val14, $val15, $val16;

    public function __construct() {
        $this->name = 'myStore';
        for($i = 1; $i <= 16; $i++) {
            $classattr = 'val'.$i;
            $this->$classattr = pow(2, $i);
        }
    }
}
$store = new myStore;

memoryusage('$store');

display_class($store, '$store at start');
memoryusage('Calling sumclassval_passbyvalue');
sumclassval_passbyvalue($store);
display_class($store, '$store after sumclassval_passbyvalue');
memoryusage('Calling sumclassval_passbyreference');
sumclassval_passbyreference($store);
display_class($store, '$store after sumclassval_passbyreference');
memoryusage('Second call to sumclassval_passbyvalue');
sumclassval_passbyvalue($store);
display_class($store, '$store after second sumclassval_passbyvalue');
memoryusage('Second call to  sumclassval_passbyreference');
sumclassval_passbyreference($store);
display_class($store, '$store after second sumclassval_passbyreference');
memoryusage('End');
?>
            </tbody>
        </table>
    </body>
</html>
