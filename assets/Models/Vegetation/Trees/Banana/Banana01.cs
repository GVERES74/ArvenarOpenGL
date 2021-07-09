
singleton TSShapeConstructor(Banana01DAE)
{
   baseShape = "./Banana01.DAE";
};

function Banana01DAE::onLoad(%this)
{
   %this.addImposter("64", "4", "0", "0", "512", "1", "0");
}
