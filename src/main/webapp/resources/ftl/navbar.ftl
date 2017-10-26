<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<@spring.url '/products'/>">Products</a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="<@spring.url '/cart'/>">Cart</a></li>
            </ul>
            <#--<ul class="navbar-form navbar-left">-->
            <#--</ul>-->
            <#--<ul class="nav navbar-nav navbar-right">-->
            <#--</ul>-->
        </div>
    </div>
</nav>